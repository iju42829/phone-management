package study.phonemanagement.controller.order.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class CreateOrderRequest {
    private CreateOrderDeliveryRequest delivery;
    private List<CreateOrderPhoneRequest> createOrderPhoneRequestList;
}
