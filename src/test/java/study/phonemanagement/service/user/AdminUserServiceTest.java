package study.phonemanagement.service.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import study.phonemanagement.IntegrationTestSupport;
import study.phonemanagement.controller.user.request.CreateUserRequest;
import study.phonemanagement.entity.user.Gender;
import study.phonemanagement.entity.user.Role;
import study.phonemanagement.entity.user.User;
import study.phonemanagement.exception.user.AlreadyExistsAdminException;
import study.phonemanagement.exception.user.UserNotFoundException;
import study.phonemanagement.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static study.phonemanagement.common.ErrorCode.ADMIN_ALREADY_EXISTS;
import static study.phonemanagement.common.ErrorCode.USER_NOT_FOUND;

class AdminUserServiceTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminUserService adminUserService;

    @DisplayName("관리자가 없으면 새로운 관리자를 생성합니다.")
    @Test
    void createAdminUser() {
        // given
        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .username("testAdmin")
                .password("testPassword")
                .confirmPassword("testPassword")
                .gender(Gender.MALE)
                .email("test@test")
                .build();

        // when
        Long adminUserId = adminUserService.createAdminUser(createUserRequest);

        // then
        User user = userRepository.findById(adminUserId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        assertThat(user)
                .extracting(User::getUsername, User::getGender, User::getEmail)
                .containsExactly(createUserRequest.getUsername(), createUserRequest.getGender(), createUserRequest.getEmail());
    }

    @DisplayName("이미 관리자가 존재하면 관리자를 새로 생성할 수 없습니다.")
    @Test
    void createAdminUserAlreadyAdmin() {
        // given
        User user = User.builder()
                .username("testUser")
                .password("testPassword")
                .gender(Gender.MALE)
                .role(Role.ADMIN)
                .email("test@test.com")
                .build();

        userRepository.save(user);

        // when - then
        assertThatThrownBy(() -> adminUserService.createAdminUser(null))
                .isInstanceOf(AlreadyExistsAdminException.class)
                .hasMessage(ADMIN_ALREADY_EXISTS.getMessage());
    }

}
