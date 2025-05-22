package study.phonemanagement.entity.inquiry;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InquiryTest {

    @DisplayName("문의에 답변을 작성하면 상태가 REPLIED로 변경되고 답변이 저장된다.")
    @Test
    void reply() {
        Inquiry inquiry = Inquiry.builder()
                .content("testContent")
                .build();

        inquiry.reply("testReply");

        assertThat(inquiry)
                .extracting(Inquiry::getContent, Inquiry::getReply, Inquiry::getInquiryStatus)
                .containsExactly("testContent", "testReply", InquiryStatus.REPLIED);
    }
}
