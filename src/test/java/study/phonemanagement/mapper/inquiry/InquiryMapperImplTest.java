package study.phonemanagement.mapper.inquiry;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import study.phonemanagement.IntegrationTestSupport;
import study.phonemanagement.controller.inquiry.request.CreateInquiryRequest;
import study.phonemanagement.entity.common.Address;
import study.phonemanagement.entity.inquiry.Inquiry;
import study.phonemanagement.entity.phone.Phone;
import study.phonemanagement.entity.user.User;
import study.phonemanagement.service.inquiry.response.DetailInquiryResponse;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static study.phonemanagement.entity.inquiry.InquiryStatus.BEFORE_REPLY;
import static study.phonemanagement.entity.phone.Manufacturer.SAMSUNG;
import static study.phonemanagement.entity.phone.Status.AVAILABLE;
import static study.phonemanagement.entity.phone.Storage.STORAGE_128;
import static study.phonemanagement.entity.user.Gender.MALE;
import static study.phonemanagement.entity.user.Role.USER;

class InquiryMapperImplTest extends IntegrationTestSupport {

    @Autowired
    private InquiryMapper inquiryMapper;

    @DisplayName("createInquiryRequest, phone, user를 Inquiry으로 변환한다.")
    @Test
    void toInquiry() {
        // given
        CreateInquiryRequest createInquiryRequest = new CreateInquiryRequest();
        createInquiryRequest.setContent("testContent");

        User user = User.builder().build();
        Phone phone = Phone.builder().build();

        // when
        Inquiry inquiry = inquiryMapper.toInquiry(createInquiryRequest, phone, user);

        // then
        assertThat(inquiry.getContent()).isEqualTo(createInquiryRequest.getContent());
        assertThat(inquiry.getUser()).isNotNull();
        assertThat(inquiry.getPhone()).isNotNull();
    }

    private static Stream<Arguments> provideNullInputsForToInquiry() {
        // given
        Phone phone = Phone.builder().build();
        User user = User.builder().build();
        CreateInquiryRequest request = new CreateInquiryRequest();

        return Stream.of(
                Arguments.of(null, phone, user),
                Arguments.of(request, null, user),
                Arguments.of(request, phone, null),
                Arguments.of(null, null, null)
        );
    }

    @MethodSource("provideNullInputsForToInquiry")
    @DisplayName("createInquiryRequest, phone, user 중 하나라도 null이면 null을 반환한다.")
    @ParameterizedTest
    void toInquiryWithAnyNull(CreateInquiryRequest createInquiryRequest, Phone phone, User user) {
        // when
        Inquiry inquiry = inquiryMapper.toInquiry(createInquiryRequest, phone, user);

        // then
        assertThat(inquiry).isNull();
    }

    @DisplayName("Inquiry를 DetailInquiryResponse으로 변환한다.")
    @Test
    void toDetailInquiryResponse() {
        // given
        User user = User.builder()
                .username("testUser")
                .password("test")
                .gender(MALE)
                .role(USER)
                .email("test@test.com")
                .address(new Address("testCity", "testStreet", "testZipcode", "testDetail"))
                .build();

        Phone phone = Phone.builder()
                .name("testPhone")
                .manufacturer(SAMSUNG)
                .storage(STORAGE_128)
                .status(AVAILABLE)
                .price(200000)
                .quantity(100)
                .color("testColor")
                .build();

        Inquiry inquiry = Inquiry.builder()
                .user(user)
                .phone(phone)
                .content("testContent")
                .inquiryStatus(BEFORE_REPLY)
                .build();

        // when
        DetailInquiryResponse detailInquiryResponse = inquiryMapper.toDetailInquiryResponse(inquiry);

        // then
        assertThat(detailInquiryResponse)
                .extracting(DetailInquiryResponse::getId, DetailInquiryResponse::getPhoneName, DetailInquiryResponse:: getContent,
                        DetailInquiryResponse::getReply, DetailInquiryResponse::getCreatedDate, DetailInquiryResponse::getLastModifiedDate)
                .containsExactly(inquiry.getId(), phone.getName(), inquiry.getContent(), null, null, null);
    }

    @DisplayName("Inquiry가 null이면 null을 반환한다.")
    @Test
    void toDetailInquiryResponseWithNull() {
        // given - when
        DetailInquiryResponse detailInquiryResponse = inquiryMapper.toDetailInquiryResponse(null);

        // then
        assertThat(detailInquiryResponse).isNull();
    }
}
