package study.phonemanagement.controller.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import study.phonemanagement.ControllerTestSupport;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AdminInitControllerTest extends ControllerTestSupport {

    @DisplayName("관리자 회원가입 페이지에 접근하면 adminJoin 뷰를 반환한다.")
    @Test
    void joinPage() throws Exception {
        // given - when - then
        mockMvc.perform(get("/admin/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/adminJoin"))
                .andExpect(model().attributeExists("createUserRequest"));
    }

    @DisplayName("회원가입 성공 시 관리자 로그인 페이지로 이동합니다.")
    @Test
    void joinAdmin() throws Exception {
        // given - when
        mockMvc.perform(
                        post("/admin/signup")
                                .contentType(APPLICATION_FORM_URLENCODED_VALUE)
                                .with(csrf())
                                .param("username", "test")
                                .param("password", "test")
                                .param("confirmPassword", "test")
                                .param("gender", "MALE")
                                .param("email", "test@test")
                                .param("city", "testCity")
                                .param("street", "testStreet")
                                .param("zipcode", "testZipcode")
                                .param("detail", "testDetail")
                )
                // then
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/login"));
    }

    @DisplayName("비밀번호와 확인 비밀번호 불일치 시 관리자 회원가입 폼을 다시 반환한다")
    @Test
    void joinAdminWithPasswordMismatch() throws Exception {
        // given - when
        mockMvc.perform(
                        post("/admin/signup")
                                .contentType(APPLICATION_FORM_URLENCODED_VALUE)
                                .with(csrf())
                                .param("username", "test")
                                .param("password", "test")
                                .param("confirmPassword", "test1")
                                .param("gender", "MALE")
                                .param("email", "test@test")
                                .param("city", "testCity")
                                .param("street", "testStreet")
                                .param("zipcode", "testZipcode")
                                .param("detail", "testDetail")
                )
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("createUserRequest"))
                .andExpect(model().attributeErrorCount("createUserRequest", 1))
                .andExpect(view().name("users/adminJoin"));
    }

    @DisplayName("모든 필드가 비어있을 때 관리자 회원가입 폼을 다시 반환한다.")
    @Test
    void joinUserWithAllFieldsEmpty() throws Exception {
        // given - when
        mockMvc.perform(
                        post("/admin/signup")
                                .contentType(APPLICATION_FORM_URLENCODED_VALUE)
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("createUserRequest"))
                .andExpect(model().attributeErrorCount("createUserRequest", 8))
                .andExpect(view().name("users/adminJoin"));
    }
}
