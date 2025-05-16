package study.phonemanagement.service.order;

import org.springframework.data.domain.Page;
import study.phonemanagement.controller.cart.request.CreateCartOrderRequest;
import study.phonemanagement.controller.order.request.CancelOrderRequest;
import study.phonemanagement.controller.order.request.CreateOrderRequest;
import study.phonemanagement.service.order.response.OrderListResponse;
import study.phonemanagement.service.user.CustomUserDetails;

public interface OrderService {
    Long createOrder(CreateOrderRequest createOrderRequest, CustomUserDetails user);
    Long createOrderByCart(CreateCartOrderRequest createCartOrderRequest, CustomUserDetails user);
    Page<OrderListResponse> getOrders(CustomUserDetails customUserDetails, Integer pageNumber, Integer pageSize);
    Page<OrderListResponse> getOrdersByUsername(String username, int pageNumber, int pageSize);

    void cancelOrder(CancelOrderRequest cancelOrderRequest, CustomUserDetails customUserDetails);
}
