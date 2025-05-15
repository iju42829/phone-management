package study.phonemanagement.service.cart;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.phonemanagement.controller.cart.request.CreateCartOrderRequest;
import study.phonemanagement.controller.cart.request.CreateCartRequest;
import study.phonemanagement.entity.cart.Cart;
import study.phonemanagement.entity.phone.Phone;
import study.phonemanagement.entity.user.User;
import study.phonemanagement.exception.phone.PhoneNotFoundException;
import study.phonemanagement.exception.user.UserNotFoundException;
import study.phonemanagement.mapper.cart.CartMapper;
import study.phonemanagement.repository.order.CartRepository;
import study.phonemanagement.repository.UserRepository;
import study.phonemanagement.repository.phone.PhoneRepository;
import study.phonemanagement.service.cart.response.CartResponse;
import study.phonemanagement.service.user.CustomUserDetails;

import java.util.List;

import static study.phonemanagement.common.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final PhoneRepository phoneRepository;
    private final UserRepository userRepository;

    private final CartMapper cartMapper;

    @Override
    public Long createCart(CreateCartRequest createCartRequest, CustomUserDetails customUserDetails) {
        User user = userRepository.findByUsername(customUserDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        Phone phone = phoneRepository.findById(createCartRequest.getPhoneId())
                .orElseThrow(() -> new PhoneNotFoundException(PHONE_NOT_FOUND));

        Cart cart = cartMapper.toCart(phone, user, createCartRequest.getCount());

        cartRepository.save(cart);

        return cart.getId();
    }

    @Override
    public List<CartResponse> getCartList(CustomUserDetails customUserDetails) {
        User user = userRepository.findByUsername(customUserDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        return cartRepository.findAllByUser(user).stream()
                .map(cartMapper::toCartResponse)
                .toList();
    }

    @Override
    public void clearCartAfterOrder(CreateCartOrderRequest createCartOrderRequest) {
        createCartOrderRequest.getCreateCartOrderPhoneRequests()
                .forEach(createCartOrderPhoneRequest -> {
                    cartRepository.deleteById(createCartOrderPhoneRequest.getCartId());
        });
    }

    @Override
    public void removeCartItem(Long cartId) {
        cartRepository.deleteById(cartId);
    }
}
