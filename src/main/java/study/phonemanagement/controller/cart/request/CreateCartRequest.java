package study.phonemanagement.controller.cart.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateCartRequest {
    private Long phoneId;
    private Integer count;
}
