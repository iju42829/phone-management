package study.phonemanagement.service.cart;

import study.phonemanagement.controller.cart.request.CreateCartOrderRequest;
import study.phonemanagement.controller.cart.request.CreateCartRequest;
import study.phonemanagement.service.cart.response.CartResponse;
import study.phonemanagement.service.user.CustomUserDetails;

import java.util.List;

public interface CartService {
    Long createCart(CreateCartRequest createCartRequest, CustomUserDetails customUserDetails);
    List<CartResponse> getCartList(CustomUserDetails customUserDetails);
    void clearCartAfterOrder(CreateCartOrderRequest createCartOrderRequest);
    void removeCartItem(Long cartId);
}
