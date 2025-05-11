package study.phonemanagement.controller.cart.request;

import lombok.Getter;
import lombok.Setter;
import study.phonemanagement.controller.order.request.CreateOrderDeliveryRequest;

import java.util.List;

@Getter @Setter
public class CreateCartOrderRequest {
    private CreateOrderDeliveryRequest delivery;
    private List<CreateCartOrderPhoneRequest> createCartOrderPhoneRequests;
}
