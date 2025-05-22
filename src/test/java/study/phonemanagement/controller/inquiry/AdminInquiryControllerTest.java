package study.phonemanagement.controller.inquiry;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import study.phonemanagement.ControllerTestSupport;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class AdminInquiryControllerTest extends ControllerTestSupport {


    @DisplayName("문의 관리자 페이지에 접근하면 InquiryAdmin 뷰를 반환합니다.")
    @WithMockUser(username="admin", roles={"ADMIN"})
    @Test
    void inquiryAdminPage() throws Exception {
        // given - when - then
        mockMvc.perform(get("/admin/inquiries"))
                .andExpect(status().isOk())
                .andExpect(view().name("inquiry/InquiryAdmin"))
                .andExpect(model().attributeExists("inquiries"))
                .andExpect(model().attributeExists("replyInquiryRequest"));
    }

    @DisplayName("답변 등록 성공 시 목록 관리 목록 페이지로 이동합니다.")
    @WithMockUser(username="admin", roles={"ADMIN"})
    @Test
    void replyInquiry() throws Exception {
        // given - when - then
        mockMvc.perform(post("/admin/inquiries/{inquiryId}", 1L)
                        .contentType(APPLICATION_FORM_URLENCODED_VALUE)
                        .param("reply", "testContent")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/inquiries"));
    }

    @DisplayName("답변 등록 실패시 답변 등록 폼을 반환합니다.")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void replyInquiryNoFields() throws Exception {
        // given - when - then
        mockMvc.perform(post("/admin/inquiries/{inquiryId}", 1L)
                        .contentType(APPLICATION_FORM_URLENCODED_VALUE)
                        .param("reply", "")
                        .with(csrf()))

                .andExpect(status().isOk())
                .andExpect(model().attributeErrorCount("replyInquiryRequest", 1))
                .andExpect(view().name("inquiry/InquiryAdmin"));
    }
}
