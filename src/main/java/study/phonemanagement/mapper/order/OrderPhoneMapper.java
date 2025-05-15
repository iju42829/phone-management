package study.phonemanagement.mapper.order;

import study.phonemanagement.entity.order.OrderPhone;
import study.phonemanagement.service.order.response.OrderPhoneDetailResponse;

public interface OrderPhoneMapper {

    OrderPhoneDetailResponse toOrderPhoneDetailResponse(OrderPhone orderPhone);
}
