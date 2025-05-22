package study.phonemanagement.controller.inquiry;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import study.phonemanagement.ControllerTestSupport;
import study.phonemanagement.controller.inquiry.request.CreateInquiryRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class InquiryControllerTest extends ControllerTestSupport {

    @DisplayName("사용자 문의 목록 페이지에 접근하면 InquiryUser 뷰를 반환합니다.")
    @WithMockUser
    @Test
    void inquiryUserPage() throws Exception {
        // given - when - then
        mockMvc.perform(get("/inquiries"))
                .andExpect(status().isOk())
                .andExpect(view().name("inquiry/InquiryUser"))
                .andExpect(model().attributeExists("inquiries"));
    }

    @DisplayName("문의 등록 페이지에 접근하면 createInquiry 뷰를 반환합니다.")
    @WithMockUser
    @Test
    void createInquiryPage() throws Exception {
        // given - when - then
        mockMvc.perform(get("/inquiries/{phoneId}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("inquiry/createInquiry"))
                .andExpect(model().attributeExists("createInquiryRequest"))
                .andExpect(model().attributeExists("phoneId"));
    }

    @DisplayName("문의 등록 성공 시 휴대폰 목록 페이지로 이동합니다.")
    @WithMockUser
    @Test
    void createInquiry() throws Exception {
        // given - when - then
        mockMvc.perform(post("/inquiries/{phoneId}", 1L)
                        .param("content", "testContent")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/phones"));

        verify(inquiryService, times(1))
                .createInquiry(any(Long.class), any(CreateInquiryRequest.class), any());
    }

    @DisplayName("문의 등록 실패시 문의 등록 폼을 반환합니다.")
    @WithMockUser
    @Test
    void createInquiryNoFields() throws Exception {
        // given - when - then
        mockMvc.perform(post("/inquiries/{phoneId}", 1L)
                        .param("content", "")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeErrorCount("createInquiryRequest", 1))
                .andExpect(view().name("inquiry/createInquiry"));

        verify(inquiryService, times(0))
                .createInquiry(any(Long.class), any(CreateInquiryRequest.class), any());
    }
}
