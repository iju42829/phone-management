package study.phonemanagement.service.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.phonemanagement.controller.cart.request.CreateCartOrderRequest;
import study.phonemanagement.service.cart.CartService;
import study.phonemanagement.service.order.OrderService;
import study.phonemanagement.service.user.CustomUserDetails;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderCartFacade {

    private final OrderService orderService;
    private final CartService cartService;

    public Long orderAndClearCart(CreateCartOrderRequest createCartOrderRequest, @AuthenticationPrincipal CustomUserDetails user) {
        Long orderByCart = orderService.createOrderByCart(createCartOrderRequest, user);

        cartService.clearCartAfterOrder(createCartOrderRequest);

        return orderByCart;
    }
}
