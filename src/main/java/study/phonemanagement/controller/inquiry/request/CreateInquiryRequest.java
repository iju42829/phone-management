package study.phonemanagement.controller.inquiry.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateInquiryRequest {
    @NotEmpty
    private String content;
}
