package study.phonemanagement.controller.order;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import study.phonemanagement.controller.cart.request.CreateCartOrderRequest;
import study.phonemanagement.service.facade.OrderCartFacade;
import study.phonemanagement.service.user.CustomUserDetails;

@Controller
@RequestMapping("/orders/carts")
@RequiredArgsConstructor
public class OrderCartController {

    private final OrderCartFacade orderCartFacade;


    @PostMapping
    public String addOrderToCart(@ModelAttribute("order") CreateCartOrderRequest createCartOrderRequest, @AuthenticationPrincipal CustomUserDetails user) {
        orderCartFacade.orderAndClearCart(createCartOrderRequest, user);

        return "redirect:/phones";
    }
}
