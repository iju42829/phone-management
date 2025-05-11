package study.phonemanagement.mapper.cart;

import org.springframework.stereotype.Component;
import study.phonemanagement.entity.cart.Cart;
import study.phonemanagement.entity.phone.Phone;
import study.phonemanagement.entity.user.User;
import study.phonemanagement.service.cart.response.CartResponse;

@Component
public class CartMapperImpl implements CartMapper{
    @Override
    public Cart toCart(Phone phone, User user, Integer count) {
        if (phone == null || user == null) {
            return null;
        }

        return Cart.builder()
                .user(user)
                .phone(phone)
                .count(count)
                .build();
    }

    @Override
    public CartResponse toCartResponse(Cart cart) {
        if (cart == null) {
            return null;
        }

        return CartResponse.builder()
                .cartId(cart.getId())
                .phoneId(cart.getPhone().getId())
                .name(cart.getPhone().getName())
                .manufacturer(cart.getPhone().getManufacturer().name())
                .storage(cart.getPhone().getStorage().name())
                .price(cart.getPhone().getPrice())
                .count(cart.getCount())
                .build();
    }
}
