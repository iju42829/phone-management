package study.phonemanagement.mapper.inquiry;

import org.springframework.stereotype.Component;
import study.phonemanagement.controller.inquiry.request.CreateInquiryRequest;
import study.phonemanagement.entity.inquiry.Inquiry;
import study.phonemanagement.entity.phone.Phone;
import study.phonemanagement.entity.user.User;

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
                .build();
    }
}
