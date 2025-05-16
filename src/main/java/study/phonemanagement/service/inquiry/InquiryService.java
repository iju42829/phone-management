package study.phonemanagement.service.inquiry;

import study.phonemanagement.controller.inquiry.request.CreateInquiryRequest;
import study.phonemanagement.controller.inquiry.request.ReplyInquiryRequest;
import study.phonemanagement.service.inquiry.response.DetailInquiryResponse;
import study.phonemanagement.service.user.CustomUserDetails;

import java.util.List;

public interface InquiryService {
    Long createInquiry(Long phoneId, CreateInquiryRequest createInquiryRequest, CustomUserDetails customUserDetails);
    List<DetailInquiryResponse> getInquiryList(CustomUserDetails customUserDetails);
    List<DetailInquiryResponse> getInquiryList();
    void replyInquiry(Long inquiryId, ReplyInquiryRequest replyInquiryRequest);
}
