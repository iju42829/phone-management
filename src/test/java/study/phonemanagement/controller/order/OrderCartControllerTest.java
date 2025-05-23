package study.phonemanagement.controller.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import study.phonemanagement.ControllerTestSupport;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderCartControllerTest extends ControllerTestSupport {

    @DisplayName("장바구니 주문에 성공하면 휴대폰 목록으로 이동합니다.")
    @WithMockUser
    @Test
    void addOrderToCart() throws Exception {
        mockMvc.perform(post("/orders/carts").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/phones"));
    }
}
