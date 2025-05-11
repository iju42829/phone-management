package study.phonemanagement.service.order;

import study.phonemanagement.controller.cart.request.CreateCartOrderRequest;
import study.phonemanagement.controller.order.request.CreateOrderRequest;
import study.phonemanagement.service.user.CustomUserDetails;

public interface OrderService {
    Long createOrder(CreateOrderRequest createOrderRequest, CustomUserDetails user);
    Long createOrderByCart(CreateCartOrderRequest createCartOrderRequest, CustomUserDetails user);
}
