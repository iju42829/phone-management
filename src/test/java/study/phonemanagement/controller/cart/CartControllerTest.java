package study.phonemanagement.controller.cart;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import study.phonemanagement.ControllerTestSupport;
import study.phonemanagement.controller.cart.request.CreateCartRequest;
import study.phonemanagement.service.cart.response.CartResponse;
import study.phonemanagement.service.user.CustomUserDetails;
import study.phonemanagement.service.user.response.AddressResponse;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static study.phonemanagement.entity.user.Role.USER;

class CartControllerTest extends ControllerTestSupport {

    @DisplayName("장바구니 추가에 성공하면 휴대폰 목록으로 이동한다.")
    @WithMockUser
    @Test
    void addPhoneToCart() throws Exception {
        mockMvc.perform(post("/carts")
                        .with(csrf())
                        .param("phoneId", "1")
                        .param("count", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/phones"));

        verify(cartService, times(1))
                .createCart(any(CreateCartRequest.class), any());
    }

    @DisplayName("장바구니 페이지 요청 시 사용자의 장바구니 목록을 반환한다")
    @WithMockUser
    @Test
    void viewCart() throws Exception {
        // given
        List<CartResponse> cartResponseList = List.of(CartResponse.builder()
                .cartId(1L)
                .phoneId(1L)
                .name("mock")
                .manufacturer("mock")
                .storage("mock")
                .price(1)
                .count(1)
                .build());

        AddressResponse addressResponse = AddressResponse.builder()
                .city("mock")
                .street("mock")
                .zipcode("mock")
                .build();

        when(cartService.getCartList(any(CustomUserDetails.class))).thenReturn(cartResponseList);
        when(userService.getUserAddress(any(CustomUserDetails.class))).thenReturn(addressResponse);

        CustomUserDetails customUserDetails = new CustomUserDetails("admin", "test", USER.getKey());

        // when - then
        mockMvc.perform(get("/carts")
                        .with(SecurityMockMvcRequestPostProcessors.user(customUserDetails)))
                .andExpect(view().name("cart/cartList"));
    }

    @DisplayName("장바구니 상품 삭제에 성공한 경우 장바구니 목록으로 이동합니다.")
    @WithMockUser
    @Test
    void deleteCartItem() throws Exception {
        mockMvc.perform(delete("/carts/{cartId}", 1L)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/carts"));
    }
}
