package study.phonemanagement.service.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import study.phonemanagement.IntegrationTestSupport;
import study.phonemanagement.controller.user.request.CreateUserRequest;
import study.phonemanagement.entity.user.User;
import study.phonemanagement.exception.user.AlreadyExistsEmailException;
import study.phonemanagement.exception.user.AlreadyExistsUsernameException;
import study.phonemanagement.repository.UserRepository;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
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
        CreateUserRequest createUserRequest = createTestUser();

        // when
        Long savedUserId = userService.createUser(createUserRequest);

        // then
        User user = userRepository
                .findById(savedUserId)
                .orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다."));

        assertThat(user)
                .extracting(User::getId, User::getUsername, User::getPassword,
                        User::getGender, User::getRole, User::getEmail)

                .contains(savedUserId,
                        createUserRequest.getUsername(), createUserRequest.getPassword(),
                        createUserRequest.getGender(), USER, createUserRequest.getEmail());
    }

    @DisplayName("이미 존재하는 아이디로 회원가입을 진행하면 예외가 발생한다.")
    @Test
    void createUserDuplicateUsername() {
        // given
        CreateUserRequest createUserRequest1 = createTestUser();
        userService.createUser(createUserRequest1);

        // when - then
        CreateUserRequest testUser = CreateUserRequest.builder()
                .username("test")
                .password("test")
                .confirmPassword("test")
                .gender(MALE)
                .email("test@test1")
                .build();

        assertThatThrownBy(() -> userService.createUser(testUser))
                .isInstanceOf(AlreadyExistsUsernameException.class)
                .hasMessage("이미 사용 중인 아이디입니다.");
    }

    @DisplayName("이미 존재하는 이메일로 회원가입을 진행하면 예외가 발생한다.")
    @Test
    void createUserDuplicateEmail() {
        // given
        CreateUserRequest createUserRequest1 = createTestUser();
        userService.createUser(createUserRequest1);

        // when - then
        CreateUserRequest testUser = CreateUserRequest.builder()
                .username("test1")
                .password("test")
                .confirmPassword("test")
                .gender(MALE)
                .email("test@test")
                .build();

        assertThatThrownBy(() -> userService.createUser(testUser))
                .isInstanceOf(AlreadyExistsEmailException.class)
                .hasMessage("이미 사용 중인 이메일입니다.");
    }

    private static CreateUserRequest createTestUser() {
        return CreateUserRequest.builder()
                .username("test")
                .password("test")
                .confirmPassword("test")
                .gender(MALE)
                .email("test@test")
                .build();
    }
}
