package study.phonemanagement.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import study.phonemanagement.IntegrationTestSupport;
import study.phonemanagement.entity.common.Address;
import study.phonemanagement.entity.user.Gender;
import study.phonemanagement.entity.user.Role;
import study.phonemanagement.entity.user.User;
import study.phonemanagement.exception.user.UserNotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static study.phonemanagement.common.ErrorCode.USER_NOT_FOUND;
import static study.phonemanagement.entity.user.Gender.FEMALE;
import static study.phonemanagement.entity.user.Gender.MALE;
import static study.phonemanagement.entity.user.Role.ADMIN;
import static study.phonemanagement.entity.user.Role.USER;

class UserRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("username으로 회원을 조회하면 해당 회원이 반환된다.")
    void findByUsername() {
        // given
        User user1 = createTestUser("test1", "test", "test@test1", MALE, USER);
        User user2 = createTestUser("test2", "test", "test@test2", FEMALE, USER);
        User user3 = createTestUser("test3", "test", "test@test3", MALE, ADMIN);

        userRepository.saveAll(List.of(user1, user2, user3));

        // when
        User user = userRepository
                .findByUsername(user1.getUsername())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        // then
        assertThat(user)
                .extracting(User::getId, User::getUsername, User::getPassword,
                        User::getGender, User::getRole, User::getEmail)
                .contains(user1.getId(), user1.getUsername(), user1.getPassword(),
                        user1.getGender(), user1.getRole(), user1.getEmail());

        assertThat(user.getAddress())
                .extracting(Address::getCity, Address::getStreet, Address::getZipcode, Address::getDetail)
                .containsExactly(user1.getAddress().getCity(), user1.getAddress().getStreet(), user1.getAddress().getZipcode(), user1.getAddress().getDetail());
    }

    @Test
    @DisplayName("username으로 회원 존재 여부를 확인할 수 있다.")
    void existsByUsername() {
        // given
        User user = createTestUser("test1", "test", "test@test1", MALE, USER);

        userRepository.save(user);

        // when
        boolean existsByUsername = userRepository.existsByUsername(user.getUsername());

        // then
        assertThat(existsByUsername).isTrue();
    }

    @Test
    @DisplayName("email로 회원 존재 여부를 확인할 수 있다.")
    void existsByEmail() {
        // given
        User user = createTestUser("test1", "test", "test@test1", MALE, USER);

        userRepository.save(user);

        // when
        boolean existsByEmail = userRepository.existsByEmail(user.getEmail());

        // then
        assertThat(existsByEmail).isTrue();
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
