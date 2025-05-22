package study.phonemanagement.service.inquiry;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import study.phonemanagement.IntegrationTestSupport;
import study.phonemanagement.controller.inquiry.request.CreateInquiryRequest;
import study.phonemanagement.controller.inquiry.request.ReplyInquiryRequest;
import study.phonemanagement.entity.common.Address;
import study.phonemanagement.entity.inquiry.Inquiry;
import study.phonemanagement.entity.inquiry.InquiryStatus;
import study.phonemanagement.entity.phone.Manufacturer;
import study.phonemanagement.entity.phone.Phone;
import study.phonemanagement.entity.user.Gender;
import study.phonemanagement.entity.user.Role;
import study.phonemanagement.entity.user.User;
import study.phonemanagement.exception.Inquiry.InquiryNotFoundException;
import study.phonemanagement.exception.phone.PhoneNotFoundException;
import study.phonemanagement.exception.user.UserNotFoundException;
import study.phonemanagement.repository.UserRepository;
import study.phonemanagement.repository.inquiry.InquiryRepository;
import study.phonemanagement.repository.phone.PhoneRepository;
import study.phonemanagement.service.inquiry.response.DetailInquiryResponse;
import study.phonemanagement.service.user.CustomUserDetails;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static study.phonemanagement.common.ErrorCode.*;
import static study.phonemanagement.entity.phone.Manufacturer.SAMSUNG;
import static study.phonemanagement.entity.phone.Status.AVAILABLE;
import static study.phonemanagement.entity.phone.Storage.STORAGE_128;
import static study.phonemanagement.entity.user.Gender.MALE;
import static study.phonemanagement.entity.user.Role.USER;

class InquiryServiceTest extends IntegrationTestSupport {

    @Autowired
    private InquiryService inquiryService;

    @Autowired
    private InquiryRepository inquiryRepository;

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        inquiryRepository.deleteAllInBatch();
        phoneRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("정상적인 문의 생성 요청 시 문의가 저장되고 ID가 반환된다.")
    @Test
    void createInquiry() {
        // given
        User user = createTestUser("testUser", "test", "test@test", MALE, USER);
        Phone phone = createTestPhoneEntity("testPhone", SAMSUNG);
        CreateInquiryRequest createInquiryRequest = new CreateInquiryRequest();
        createInquiryRequest.setContent("testContent");

        userRepository.save(user);
        phoneRepository.save(phone);

        CustomUserDetails customUserDetails = new CustomUserDetails(user.getUsername(), user.getPassword(), user.getRole().getKey());

        // when
        Long inquiryId = inquiryService.createInquiry(phone.getId(), createInquiryRequest, customUserDetails);

        // then
        Inquiry inquiry = inquiryRepository.findById(inquiryId).orElseThrow(() -> new InquiryNotFoundException(INQUIRY_NOT_FOUND));

        assertThat(inquiry.getContent()).isEqualTo(createInquiryRequest.getContent());
        assertThat(inquiry.getUser()).isNotNull();
        assertThat(inquiry.getPhone()).isNotNull();
    }

    @DisplayName("존재하지 않는 사용자로 문의를 생성하면 예외가 발생한다.")
    @Test
    void createInquiryWithUserNullInputs() {
        // given
        Phone phone = createTestPhoneEntity("testPhone", SAMSUNG);
        phoneRepository.save(phone);

        CreateInquiryRequest createInquiryRequest = new CreateInquiryRequest();
        createInquiryRequest.setContent("testContent");

        CustomUserDetails customUserDetails = new CustomUserDetails("testUser", "test", USER.name());

        // when
        assertThatThrownBy(() -> inquiryService.createInquiry(phone.getId(), createInquiryRequest, customUserDetails))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage(USER_NOT_FOUND.getMessage());
    }

    @DisplayName("존재하지 않는 휴대폰으로 문의를 생성하면 예외가 발생한다.")
    @Test
    void createInquiryWithPhoneNullInputs() {
        // given
        User user = createTestUser("testUser", "test", "test@test", MALE, USER);
        CreateInquiryRequest createInquiryRequest = new CreateInquiryRequest();
        createInquiryRequest.setContent("testContent");

        userRepository.save(user);

        CustomUserDetails customUserDetails = new CustomUserDetails(user.getUsername(), user.getPassword(), user.getRole().getKey());

        // when
        assertThatThrownBy(() -> inquiryService.createInquiry(1L, createInquiryRequest, customUserDetails))
                .isInstanceOf(PhoneNotFoundException.class)
                .hasMessage(PHONE_NOT_FOUND.getMessage());
    }

    @DisplayName("문의 전체 목록을 조회한다.")
    @Test
    void getInquiryList() {
        // given
        User user = createTestUser("testUser", "test", "test@test", MALE, USER);
        Phone phone = createTestPhoneEntity("testPhone", SAMSUNG);
        CreateInquiryRequest createInquiryRequest = new CreateInquiryRequest();
        createInquiryRequest.setContent("testContent");

        userRepository.save(user);
        phoneRepository.save(phone);
        CustomUserDetails customUserDetails = new CustomUserDetails(user.getUsername(), user.getPassword(), user.getRole().getKey());

        Long inquiryId = inquiryService.createInquiry(phone.getId(), createInquiryRequest, customUserDetails);

        // when
        List<DetailInquiryResponse> inquiryList = inquiryService.getInquiryList();

        // then
        assertThat(inquiryList).hasSize(1);
        assertThat(inquiryList.get(0)).extracting(DetailInquiryResponse::getId, DetailInquiryResponse::getPhoneName, DetailInquiryResponse::getContent, DetailInquiryResponse::getReply)
                .containsExactly(inquiryId, phone.getName(), createInquiryRequest.getContent(), null);

        assertThat(inquiryList.get(0).getCreatedDate()).isNotNull();
        assertThat(inquiryList.get(0).getLastModifiedDate()).isNotNull();
    }

    @DisplayName("로그인한 사용자의 문의 목록을 조회한다.")
    @Test
    void getInquiryListWithCustomUserDetails() {
        // given
        User user = createTestUser("testUser", "test", "test@test", MALE, USER);
        Phone phone = createTestPhoneEntity("testPhone", SAMSUNG);
        CreateInquiryRequest createInquiryRequest = new CreateInquiryRequest();
        createInquiryRequest.setContent("testContent");

        userRepository.save(user);
        phoneRepository.save(phone);
        CustomUserDetails customUserDetails = new CustomUserDetails(user.getUsername(), user.getPassword(), user.getRole().getKey());

        Long inquiryId = inquiryService.createInquiry(phone.getId(), createInquiryRequest, customUserDetails);

        // when
        List<DetailInquiryResponse> inquiryList = inquiryService.getInquiryList(customUserDetails);

        // then
        assertThat(inquiryList).hasSize(1);
        assertThat(inquiryList.get(0)).extracting(DetailInquiryResponse::getId, DetailInquiryResponse::getPhoneName, DetailInquiryResponse::getContent, DetailInquiryResponse::getReply)
                .containsExactly(inquiryId, phone.getName(), createInquiryRequest.getContent(), null);

        assertThat(inquiryList.get(0).getCreatedDate()).isNotNull();
        assertThat(inquiryList.get(0).getLastModifiedDate()).isNotNull();
    }

    @DisplayName("존재하지 않는 사용자로 문의 목록 조회 시 예외가 발생한다.")
    @Test
    void getInquiryListWithInvalidUser() {
        // given
        User user = createTestUser("testUser", "test", "test@test", MALE, USER);
        Phone phone = createTestPhoneEntity("testPhone", SAMSUNG);

        phoneRepository.save(phone);
        CustomUserDetails customUserDetails = new CustomUserDetails(user.getUsername(), user.getPassword(), user.getRole().getKey());

        // when - then
        assertThatThrownBy(() -> inquiryService.getInquiryList(new CustomUserDetails("testUser1", "test", USER.name())))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage(USER_NOT_FOUND.getMessage());
    }

    @DisplayName("관리자가 문의에 답변을 등록하면 상태가 REPLIED로 변경되고 답변이 저장된다.")
    @Test
    void replyInquiry() {
        // given
        User user = createTestUser("testUser", "test", "test@test", MALE, USER);
        Phone phone = createTestPhoneEntity("testPhone", SAMSUNG);
        CreateInquiryRequest createInquiryRequest = new CreateInquiryRequest();
        createInquiryRequest.setContent("testContent");

        userRepository.save(user);
        phoneRepository.save(phone);

        CustomUserDetails customUserDetails = new CustomUserDetails(user.getUsername(), user.getPassword(), user.getRole().getKey());

        Long inquiryId = inquiryService.createInquiry(phone.getId(), createInquiryRequest, customUserDetails);

        ReplyInquiryRequest replyInquiryRequest = new ReplyInquiryRequest();
        replyInquiryRequest.setReply("testReply");

        // when
        inquiryService.replyInquiry(inquiryId, replyInquiryRequest);

        // then
        Inquiry inquiry = inquiryRepository.findById(inquiryId).orElseThrow(() -> new InquiryNotFoundException(INQUIRY_NOT_FOUND));

        assertThat(inquiry).extracting(Inquiry::getReply, Inquiry::getInquiryStatus)
                .containsExactly(replyInquiryRequest.getReply(), InquiryStatus.REPLIED);
    }

    @DisplayName("존재하지 않는 문의 ID로 답변을 등록하려고 하면 예외가 발생한다.")
    @Test
    void replyInquiryWithNotExistInquiryId() {
        // given - when - then
        assertThatThrownBy(() -> inquiryService.replyInquiry(1L, new ReplyInquiryRequest()))
                .isInstanceOf(InquiryNotFoundException.class)
                .hasMessage(INQUIRY_NOT_FOUND.getMessage());
    }

    private Phone createTestPhoneEntity(String name, Manufacturer manufacturer) {
        return Phone.builder()
                .name(name)
                .manufacturer(manufacturer)
                .storage(STORAGE_128)
                .status(AVAILABLE)
                .price(200000)
                .quantity(100)
                .color("testColor")
                .build();
    }

    private User createTestUser(String username, String password, String email, Gender gender, Role role) {
        return User.builder()
                .username(username)
                .password(password)
                .gender(gender)
                .role(role)
                .email(email)
                .address(new Address("testCity", "testStreet", "testZipcode", "testDetail"))
                .build();
    }
}
