package study.phonemanagement.controller.order.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import study.phonemanagement.entity.order.DeliveryStatus;

@Getter @Setter
public class UpdateDeliveryStatusRequest {
    @NotNull
    private Long orderId;

    @NotNull
    private DeliveryStatus deliveryStatus;
}
