package study.phonemanagement.controller.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import study.phonemanagement.ControllerTestSupport;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AdminAuthControllerTest extends ControllerTestSupport {

    @DisplayName("관리자 로그인 페이지에 접근하면 login 뷰를 반환합니다.")
    @Test
    void adminLoginPage() throws Exception {
        mockMvc.perform(
                        get("/admin/login")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("admin/login"));
    }

    @DisplayName("로그아웃 성공 시 관리자 로그인 페이지로 이동합니다.")
    @Test
    @WithMockUser(username="admin", roles={"ADMIN"})
    void adminLogout() throws Exception {
        mockMvc.perform(get("/admin/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/login"));
    }

    @DisplayName("인증되지 않는 사용자가 로그아웃 시도시 관리자 페이지로 이동합니다.")
    @Test
    void adminLogoutWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/admin/logout"))
                .andExpect(status().is3xxRedirection());

    }
}
