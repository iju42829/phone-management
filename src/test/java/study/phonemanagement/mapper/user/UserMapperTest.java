package study.phonemanagement.mapper.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import study.phonemanagement.IntegrationTestSupport;
import study.phonemanagement.controller.user.request.CreateUserRequest;
import study.phonemanagement.entity.common.Address;
import study.phonemanagement.entity.user.User;
import study.phonemanagement.service.user.response.AddressResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static study.phonemanagement.entity.user.Gender.MALE;

class UserMapperTest extends IntegrationTestSupport {

    @Autowired
    private UserMapper userMapper;

    @DisplayName("CreateUserRequest를 User로 변환한다.")
    @Test
    void toUser() {
        CreateUserRequest request = CreateUserRequest.builder()
                .username("test")
                .password("test")
                .confirmPassword("test")
                .gender(MALE)
                .email("test@test")
                .city("testCity")
                .street("testStreet")
                .zipcode("testZipcode")
                .detail("testDetail")
                .build();

        // when
        User user = userMapper.toUser(request);

        // then
        assertThat(user)
                .extracting(User::getUsername, User::getPassword, User::getGender, User::getEmail)
                .containsExactly(request.getUsername(), request.getPassword(), request.getGender(), request.getEmail());

        assertThat(user.getAddress())
            .extracting(Address::getCity, Address::getStreet, Address::getZipcode, Address::getDetail)
            .containsExactly(request.getCity(), request.getStreet(), request.getZipcode(), request.getDetail());
    }

    @DisplayName("CreateUserRequest가 null이면 null을 반환한다.")
    @Test
    void toUserWithNull() {
        // given - when
        User user = userMapper.toUser(null);

        // then
        assertThat(user).isNull();
    }

    @DisplayName("User의 Address를 toAddressResponse으로 변환한다.")
    @Test
    void toAddressResponse() {
        // given
        User user = User.builder()
                .address(new Address("testCity", "testStreet", "testZipcode", "testDetail"))
                .build();

        // when
        AddressResponse addressResponse = userMapper.toAddressResponse(user);

        // then
        assertThat(addressResponse)
                .extracting(AddressResponse::getCity, AddressResponse::getStreet, AddressResponse::getZipcode, AddressResponse::getDetail)
                .containsExactly(user.getAddress().getCity(), user.getAddress().getStreet(), user.getAddress().getZipcode(), user.getAddress().getDetail());
    }

    @DisplayName("User가 null 이면 null을 반환한다.")
    @Test
    void toAddressResponseWithNull() {
        // given - when
        AddressResponse addressResponse = userMapper.toAddressResponse(null);

        // then
        assertThat(addressResponse).isNull();
    }
}
