package study.phonemanagement.mapper.order;

import study.phonemanagement.entity.order.Order;
import study.phonemanagement.service.order.response.OrderListResponse;
import study.phonemanagement.service.order.response.OrderPhoneDetailResponse;

import java.util.List;

public interface OrderMapper {
    OrderListResponse toOrderListResponse(Order order, List<OrderPhoneDetailResponse> orderPhoneDetailResponseList);
}
