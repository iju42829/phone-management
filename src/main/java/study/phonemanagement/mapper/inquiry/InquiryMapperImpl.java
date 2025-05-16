package study.phonemanagement.mapper.inquiry;

import org.springframework.stereotype.Component;
import study.phonemanagement.controller.inquiry.request.CreateInquiryRequest;
import study.phonemanagement.entity.inquiry.Inquiry;
import study.phonemanagement.entity.phone.Phone;
import study.phonemanagement.entity.user.User;
import study.phonemanagement.service.inquiry.response.DetailInquiryResponse;

import static study.phonemanagement.entity.inquiry.InquiryStatus.BEFORE_REPLY;

@Component
public class InquiryMapperImpl implements InquiryMapper{

    @Override
    public Inquiry toInquiry(CreateInquiryRequest createInquiryRequest, Phone phone, User user) {
        if (createInquiryRequest == null || phone == null || user == null) {
            return null;
        }

        return Inquiry.builder()
                .phone(phone)
                .user(user)
                .content(createInquiryRequest.getContent())
                .inquiryStatus(BEFORE_REPLY)
                .build();
    }

    @Override
    public DetailInquiryResponse toDetailInquiryResponse(Inquiry inquiry) {
        if (inquiry == null) {
            return null;
        }

        return DetailInquiryResponse.builder()
                .id(inquiry.getId())
                .phoneName(inquiry.getPhone().getName())
                .content(inquiry.getContent())
                .reply(inquiry.getReply())
                .createdDate(inquiry.getCreatedDate())
                .lastModifiedDate(inquiry.getLastModifiedDate())
                .build();
    }
}
