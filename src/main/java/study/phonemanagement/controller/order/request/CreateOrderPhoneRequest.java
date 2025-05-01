package study.phonemanagement.controller.order.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateOrderPhoneRequest {
    private Long phoneId;
    private Integer count;
}
