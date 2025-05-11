package study.phonemanagement.controller.cart;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import study.phonemanagement.controller.cart.request.CreateCartRequest;
import study.phonemanagement.controller.order.request.CreateOrderDeliveryRequest;
import study.phonemanagement.controller.order.request.CreateOrderPhoneRequest;
import study.phonemanagement.controller.order.request.CreateOrderRequest;
import study.phonemanagement.service.cart.CartService;
import study.phonemanagement.service.cart.response.CartResponse;
import study.phonemanagement.service.user.CustomUserDetails;
import study.phonemanagement.service.user.UserService;
import study.phonemanagement.service.user.response.AddressResponse;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    @PostMapping
    public String addPhoneToCart(@ModelAttribute CreateCartRequest createCartRequest, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        cartService.createCart(createCartRequest, customUserDetails);

        return "redirect:/phones";
    }

    @GetMapping("/cart")
    public String viewCart(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        List<CartResponse> cartItems = cartService.getCartList(customUserDetails);
        AddressResponse addressResponse = userService.getUserAddress(customUserDetails);

        CreateOrderRequest orderRequest = new CreateOrderRequest();
        List<CreateOrderPhoneRequest> phones = cartItems.stream()
                .map(item -> new CreateOrderPhoneRequest(item.getPhoneId(), item.getCount()))
                .toList();

        orderRequest.setCreateOrderPhoneRequestList(phones);
        orderRequest.setDelivery(CreateOrderDeliveryRequest
                .createOrderDeliveryRequest(addressResponse.getCity(), addressResponse.getStreet(), addressResponse.getZipcode(), addressResponse.getDetail()));

        model.addAttribute("order", orderRequest);
        model.addAttribute("phone", cartItems);

        return "cart/cartList";
    }
}
