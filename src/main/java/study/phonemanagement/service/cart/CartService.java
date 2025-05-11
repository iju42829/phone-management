package study.phonemanagement.service.cart;

import study.phonemanagement.controller.cart.request.CreateCartRequest;
import study.phonemanagement.service.user.CustomUserDetails;

public interface CartService {
    Long createCart(CreateCartRequest createCartRequest, CustomUserDetails customUserDetails);
}
