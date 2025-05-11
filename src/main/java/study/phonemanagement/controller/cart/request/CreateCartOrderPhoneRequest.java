package study.phonemanagement.controller.cart.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class CreateCartOrderPhoneRequest {
    private Long cartId;
    private Long phoneId;
    private Integer count;

    public CreateCartOrderPhoneRequest(Long cartId, Long phoneId, int count) {
        this.cartId = cartId;
        this.phoneId = phoneId;
        this.count = count;
    }
}
