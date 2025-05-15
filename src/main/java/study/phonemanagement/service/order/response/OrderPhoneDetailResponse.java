package study.phonemanagement.service.order.response;

import lombok.*;

@Builder
@Getter @Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderPhoneDetailResponse {
    private Long phoneId;
    private String name;
    private String manufacturer;
    private Integer count;
    private Integer orderPrice;
}
