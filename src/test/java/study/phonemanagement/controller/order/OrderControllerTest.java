package study.phonemanagement.controller.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import study.phonemanagement.ControllerTestSupport;
import study.phonemanagement.controller.order.request.CreateOrderRequest;

import study.phonemanagement.service.order.response.OrderListResponse;
import study.phonemanagement.service.phone.response.DetailPhoneResponse;
import study.phonemanagement.service.user.CustomUserDetails;
import study.phonemanagement.service.user.response.AddressResponse;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static study.phonemanagement.entity.user.Role.ADMIN;
import static study.phonemanagement.entity.user.Role.USER;

class OrderControllerTest extends ControllerTestSupport {

    @DisplayName("휴대폰 주문 페이지에 접근하면 orderPhone 뷰를 반환한다.")
    @WithMockUser
    @Test
    void orderPage() throws Exception {
        // given
        DetailPhoneResponse detailPhoneResponse = DetailPhoneResponse.builder().build();
        AddressResponse addressResponse = AddressResponse.builder().build();

        when(phoneService.getPhoneForDetail(any(Long.class)))
                .thenReturn(detailPhoneResponse);
        when(userService.getUserAddress(any()))
                .thenReturn(addressResponse);

        // when - then
        mockMvc.perform(get("/orders/{phoneId}", 1L))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("phone"))
                .andExpect(model().attributeExists("order"))
                .andExpect(view().name("order/orderPhone"));

        verify(phoneService, times(1))
                .getPhoneForDetail(any(Long.class));

        verify(userService, times(1))
                .getUserAddress(any());
    }

    @DisplayName("사용자의 주문 내역만 모델에 담아 list 뷰를 반환한다")
    @WithMockUser
    @Test
    void getOrderList() throws Exception {
        // given - when
        List<OrderListResponse> orderListResponses = List.of(OrderListResponse.builder().build());
        when(orderService.getOrders(any(), any(Integer.class), any(Integer.class)))
                .thenReturn(new PageImpl<>(orderListResponses, PageRequest.of(0, 10), orderListResponses.size()));

        // then
        mockMvc.perform((get("/orders")))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attributeExists("page"))
                .andExpect(model().attributeExists("pageNumbers"))
                .andExpect(model().attributeExists("startPage"))
                .andExpect(model().attributeExists("endPage"))
                .andExpect(view().name("order/list"));

        verify(orderService, times(1))
                .getOrders(any(), any(Integer.class), any(Integer.class));
    }

    @DisplayName("주문 취소 요청 성공시 유저는 사용자 주문 목록 페이지로 이동한다.")
    @WithMockUser
    @Test
    void cancelOrder_asUser() throws Exception {
        CustomUserDetails customUserDetails = new CustomUserDetails("admin", "test", USER.getKey());

        mockMvc.perform(patch("/orders")
                        .with(SecurityMockMvcRequestPostProcessors.user(customUserDetails))
                        .param("orderId", "1")
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders"));
    }

    @DisplayName("주문 취소 요청 성공시 관리자 주문 관리 목록 페이지로 이동한다.")
    @Test
    void cancelOrder_asAdmin() throws Exception {
        // given
        CustomUserDetails customUserDetails = new CustomUserDetails("admin", "test", ADMIN.getKey());

        // when - then
        mockMvc.perform(patch("/orders")
                        .with(SecurityMockMvcRequestPostProcessors.user(customUserDetails))
                        .param("orderId", "1")
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/orders"));
    }

    @DisplayName("주문에 성공하면 휴대폰 목록 페이지로 이동한다.")
    @WithMockUser
    @Test
    void addOrder() throws Exception {
        // given - when - then
        mockMvc.perform(post("/orders").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/phones"));

        verify(orderService, times(1))
                .createOrder(any(CreateOrderRequest.class), any());
    }
}
