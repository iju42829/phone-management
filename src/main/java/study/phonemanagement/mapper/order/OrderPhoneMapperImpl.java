package study.phonemanagement.mapper.order;

import org.springframework.stereotype.Component;
import study.phonemanagement.entity.order.OrderPhone;
import study.phonemanagement.service.order.response.OrderPhoneDetailResponse;

@Component
public class OrderPhoneMapperImpl implements OrderPhoneMapper {
    @Override
    public OrderPhoneDetailResponse toOrderPhoneDetailResponse(OrderPhone orderPhone) {
        if (orderPhone == null) {
            return null;
        }

        return OrderPhoneDetailResponse.builder()
                .phoneId(orderPhone.getPhone().getId())
                .name(orderPhone.getPhone().getName())
                .manufacturer(orderPhone.getPhone().getManufacturer().name())
                .count(orderPhone.getCount())
                .orderPrice(orderPhone.getOrderPrice())
                .build();
    }
}
