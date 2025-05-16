package study.phonemanagement.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.phonemanagement.common.ErrorCode;
import study.phonemanagement.controller.order.request.UpdateDeliveryStatusRequest;
import study.phonemanagement.entity.order.Order;
import study.phonemanagement.exception.order.OrderNotFoundException;
import study.phonemanagement.repository.order.OrderRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final OrderRepository orderRepository;

    @Override
    public void changeDeliveryStatus(UpdateDeliveryStatusRequest updateDeliveryStatusRequest) {
        Order order = orderRepository.findWithDeliveryById(updateDeliveryStatusRequest.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException(ErrorCode.ORDER_NOT_FOUND));

        order.getDelivery().changeStatus(updateDeliveryStatusRequest.getDeliveryStatus());
    }
}
