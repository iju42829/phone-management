package study.phonemanagement.service.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import study.phonemanagement.IntegrationTestSupport;
import study.phonemanagement.controller.user.request.CreateUserRequest;
import study.phonemanagement.entity.common.Address;
import study.phonemanagement.entity.user.User;
import study.phonemanagement.exception.user.AlreadyExistsEmailException;
import study.phonemanagement.exception.user.AlreadyExistsUsernameException;
import study.phonemanagement.exception.user.UserNotFoundException;
import study.phonemanagement.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static study.phonemanagement.common.ErrorCode.*;
import static study.phonemanagement.entity.user.Gender.MALE;
import static study.phonemanagement.entity.user.Role.USER;

class UserServiceTest extends IntegrationTestSupport {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @DisplayName("회원가입을 성공하면 회원이 저장된다.")
    @Test
    void createUser() {
        // given
        CreateUserRequest createUserRequest = createTestUser("test", "test@test");

        // when
        Long savedUserId = userService.createUser(createUserRequest);

        // then
        User user = userRepository
                .findById(savedUserId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        assertThat(user)
                .extracting(User::getId, User::getUsername, User::getPassword,
                        User::getGender, User::getRole, User::getEmail)

                .contains(savedUserId,
                        createUserRequest.getUsername(), createUserRequest.getPassword(),
                        createUserRequest.getGender(), USER, createUserRequest.getEmail());

        assertThat(user.getAddress())
            .extracting(Address::getCity, Address::getStreet, Address::getZipcode, Address::getDetail)
            .containsExactly(createUserRequest.getCity(), createUserRequest.getStreet(), createUserRequest.getZipcode(), createUserRequest.getDetail());
    }

    @DisplayName("이미 존재하는 아이디로 회원가입을 진행하면 예외가 발생한다.")
    @Test
    void createUserDuplicateUsername() {
        // given
        CreateUserRequest createUserRequest1 = createTestUser("test", "test@test");
        userService.createUser(createUserRequest1);

        // when - then
        CreateUserRequest testUser = createTestUser("test", "test@test1");

        assertThatThrownBy(() -> userService.createUser(testUser))
                .isInstanceOf(AlreadyExistsUsernameException.class)
                .hasMessage(USER_DUPLICATE_USERNAME.getMessage());
    }

    @DisplayName("이미 존재하는 이메일로 회원가입을 진행하면 예외가 발생한다.")
    @Test
    void createUserDuplicateEmail() {
        // given
        CreateUserRequest createUserRequest1 = createTestUser("test", "test@test");
        userService.createUser(createUserRequest1);

        // when - then
        CreateUserRequest testUser = createTestUser("test1", "test@test");

        assertThatThrownBy(() -> userService.createUser(testUser))
                .isInstanceOf(AlreadyExistsEmailException.class)
                .hasMessage(USER_DUPLICATE_EMAIL.getMessage());
    }

    private static CreateUserRequest createTestUser(String username, String email) {
        return CreateUserRequest.builder()
                .username(username)
                .password("test")
                .confirmPassword("test")
                .gender(MALE)
                .email(email)
                .city("testCity")
                .street("testStreet")
                .zipcode("testZipcode")
                .detail("testDetail")
                .build();
    }
}
