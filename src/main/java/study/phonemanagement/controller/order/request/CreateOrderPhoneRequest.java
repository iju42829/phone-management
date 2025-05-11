package study.phonemanagement.controller.order.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class CreateOrderPhoneRequest {
    private Long phoneId;
    private Integer count;

    public CreateOrderPhoneRequest(Long phoneId, int count) {
        this.phoneId = phoneId;
        this.count = count;
    }
}
