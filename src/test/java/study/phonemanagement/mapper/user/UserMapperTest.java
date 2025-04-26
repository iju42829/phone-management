package study.phonemanagement.mapper.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import study.phonemanagement.IntegrationTestSupport;
import study.phonemanagement.controller.user.request.CreateUserRequest;
import study.phonemanagement.entity.user.User;

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
                .build();

        // when
        User user = userMapper.toUser(request);

        // then
        assertThat(user)
                .extracting(User::getUsername, User::getPassword, User::getGender, User::getEmail)
                .containsExactly(request.getUsername(), request.getPassword(), request.getGender(), request.getEmail());
    }

    @DisplayName("CreateUserRequest가 null이면 null을 반환한다.")
    @Test
    void toUserWithNull() {
        // given - when
        User user = userMapper.toUser(null);

        // then
        assertThat(user).isNull();
    }
}
