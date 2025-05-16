package study.phonemanagement.service.inquiry;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.phonemanagement.controller.inquiry.request.CreateInquiryRequest;
import study.phonemanagement.entity.inquiry.Inquiry;
import study.phonemanagement.entity.phone.Phone;
import study.phonemanagement.entity.user.User;
import study.phonemanagement.exception.phone.PhoneNotFoundException;
import study.phonemanagement.exception.user.UserNotFoundException;
import study.phonemanagement.mapper.inquiry.InquiryMapper;
import study.phonemanagement.repository.UserRepository;
import study.phonemanagement.repository.inquiry.InquiryRepository;
import study.phonemanagement.repository.phone.PhoneRepository;
import study.phonemanagement.service.user.CustomUserDetails;

import static study.phonemanagement.common.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService {

    private final InquiryRepository inquiryRepository;
    private final PhoneRepository phoneRepository;
    private final UserRepository userRepository;

    private final InquiryMapper inquiryMapper;

    @Override
    public Long createInquiry(Long phoneId, CreateInquiryRequest createInquiryRequest, CustomUserDetails customUserDetails) {
        User user = userRepository.findByUsername(customUserDetails.getUsername()).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
        Phone phone = phoneRepository.findById(phoneId).orElseThrow(() -> new PhoneNotFoundException(PHONE_NOT_FOUND));

        Inquiry inquiry = inquiryMapper.toInquiry(createInquiryRequest, phone, user);

        inquiryRepository.save(inquiry);

        return inquiry.getId();
    }
}
