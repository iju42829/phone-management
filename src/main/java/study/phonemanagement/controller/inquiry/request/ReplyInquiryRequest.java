package study.phonemanagement.controller.inquiry.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReplyInquiryRequest {
    @NotEmpty
    private String reply;
}
