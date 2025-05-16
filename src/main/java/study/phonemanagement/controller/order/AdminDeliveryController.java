package study.phonemanagement.controller.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import study.phonemanagement.controller.order.request.UpdateDeliveryStatusRequest;
import study.phonemanagement.service.order.DeliveryService;

@Controller
@RequestMapping("/admin/deliveries")
@RequiredArgsConstructor
public class AdminDeliveryController {

    private final DeliveryService deliveryService;

    @PatchMapping
    public String updateDeliveryStatus(@ModelAttribute UpdateDeliveryStatusRequest updateDeliveryStatusRequest) {
        deliveryService.changeDeliveryStatus(updateDeliveryStatusRequest);

        return "redirect:/admin/orders";
    }
}
