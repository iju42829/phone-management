package study.phonemanagement.controller.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import study.phonemanagement.ControllerTestSupport;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest extends ControllerTestSupport {

    @DisplayName("로그인 페이지에 접근하면 login 뷰를 반환합니다.")
    @Test
    void showJoinPage() throws Exception {
        mockMvc.perform(
                        get("/users/login")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("users/login"));
    }

    @DisplayName("로그아웃 요청 시 로그아웃 후 login 페이지로 리다이렉트 합니다.")
    @WithMockUser
    @Test
    void logout() throws Exception {
        mockMvc.perform(
                        get("/users/logout")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/login"));
    }

    @DisplayName("권한이 없는 사용자가 로그아웃 요청 시 login 페이지로 리다이렉트 합니다.")
    @WithAnonymousUser
    @Test
    void logoutWithoutAuthentication() throws Exception {
        mockMvc.perform(
                        get("/users/logout")
                )
                .andExpect(status().is3xxRedirection());
    }
}
