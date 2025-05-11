package study.phonemanagement.controller.cart;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import study.phonemanagement.controller.cart.request.CreateCartRequest;
import study.phonemanagement.service.cart.CartService;
import study.phonemanagement.service.user.CustomUserDetails;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    @PostMapping
    public String addPhoneToCart(@ModelAttribute CreateCartRequest createCartRequest, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        cartService.createCart(createCartRequest, customUserDetails);

        return "redirect:/phones";
    }
}
