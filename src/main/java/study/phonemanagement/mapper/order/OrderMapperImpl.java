package study.phonemanagement.mapper.order;

import org.springframework.stereotype.Component;
import study.phonemanagement.entity.order.Order;
import study.phonemanagement.service.order.response.OrderListResponse;
import study.phonemanagement.service.order.response.OrderPhoneDetailResponse;

import java.util.List;

@Component
public class OrderMapperImpl implements OrderMapper {
    @Override
    public OrderListResponse toOrderListResponse(Order order, List<OrderPhoneDetailResponse> orderPhoneDetailResponseList) {
        if (order == null || orderPhoneDetailResponseList == null) {
            return null;
        }

        return OrderListResponse.builder()
                .orderId(order.getId())
                .orderedAt(order.getCreatedDate())
                .orderStatus(order.getStatus().name())
                .deliveryStatus(order.getDelivery().getStatus().name())
                .totalAmount(order.getTotalPrice())
                .orderPhoneDetailResponseList(orderPhoneDetailResponseList)
                .build();
    }
}
