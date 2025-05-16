package study.phonemanagement.mapper.inquiry;

import study.phonemanagement.controller.inquiry.request.CreateInquiryRequest;
import study.phonemanagement.entity.inquiry.Inquiry;
import study.phonemanagement.entity.phone.Phone;
import study.phonemanagement.entity.user.User;

public interface InquiryMapper {
    Inquiry toInquiry(CreateInquiryRequest createInquiryRequest, Phone phone, User user);
}
