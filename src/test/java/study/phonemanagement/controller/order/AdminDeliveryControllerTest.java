package study.phonemanagement.controller.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import study.phonemanagement.ControllerTestSupport;
import study.phonemanagement.controller.order.request.UpdateDeliveryStatusRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminDeliveryControllerTest extends ControllerTestSupport {

    @DisplayName("배달 상태 변경에 성공하면 관리자 주문 관리 페이지로 이동합니다.")
    @WithMockUser(username="admin", roles={"ADMIN"})
    @Test
    void updateDeliveryStatus() throws Exception {
        // given - when - then
        mockMvc.perform(patch("/admin/deliveries")
                        .with(csrf())
                        .param("orderId", "1")
                        .param("deliveryStatus", "DELIVERED"))

                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/orders"));

        verify(deliveryService, times(1))
                .changeDeliveryStatus(any(UpdateDeliveryStatusRequest.class));
    }
}
