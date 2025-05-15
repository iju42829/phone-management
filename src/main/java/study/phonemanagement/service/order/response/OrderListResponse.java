package study.phonemanagement.service.order.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter @Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderListResponse {
    private Long orderId;
    private LocalDateTime orderedAt;
    private String orderStatus;
    private String deliveryStatus;
    private Integer totalAmount;
    private List<OrderPhoneDetailResponse> orderPhoneDetailResponseList;
}
