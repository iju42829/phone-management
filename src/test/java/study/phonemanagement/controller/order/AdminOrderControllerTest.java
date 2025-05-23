package study.phonemanagement.controller.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import study.phonemanagement.ControllerTestSupport;
import study.phonemanagement.service.order.response.OrderListResponse;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AdminOrderControllerTest extends ControllerTestSupport {

    @DisplayName("관리자가 username 파라미터로 조회하면 해당 유저의 주문 내역만 모델에 담아 order/adminList 뷰를 반환한다")
    @WithMockUser(username="admin", roles={"ADMIN"})
    @Test
    void getOrderList() throws Exception {
        // given - when
        List<OrderListResponse> orderListResponses = List.of(OrderListResponse.builder().build());
        when(orderService.getOrdersByUsername(any(String.class), any(Integer.class), any(Integer.class)))
                .thenReturn(new PageImpl<>(orderListResponses, PageRequest.of(0, 10), orderListResponses.size()));

        // then
        mockMvc.perform((get("/admin/orders"))
                        .param("username", "admin"))
                .andExpect(status().isOk())

                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attributeExists("page"))
                .andExpect(model().attributeExists("pageNumbers"))
                .andExpect(model().attributeExists("startPage"))
                .andExpect(model().attributeExists("endPage"))

                .andExpect(view().name("order/adminList"));

        verify(orderService, times(1))
                .getOrdersByUsername(any(String.class), any(Integer.class), any(Integer.class));
    }
}
