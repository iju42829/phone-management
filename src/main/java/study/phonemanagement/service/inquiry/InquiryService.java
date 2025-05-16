package study.phonemanagement.service.inquiry;

import study.phonemanagement.controller.inquiry.request.CreateInquiryRequest;
import study.phonemanagement.service.user.CustomUserDetails;

public interface InquiryService {
    Long createInquiry(Long phoneId, CreateInquiryRequest createInquiryRequest, CustomUserDetails customUserDetails);
}
