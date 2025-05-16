package study.phonemanagement.service.order;

import study.phonemanagement.controller.order.request.UpdateDeliveryStatusRequest;

public interface DeliveryService {
    void changeDeliveryStatus(UpdateDeliveryStatusRequest updateDeliveryStatusRequest);
}
