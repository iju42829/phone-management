package study.phonemanagement.service.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import study.phonemanagement.IntegrationTestSupport;
import study.phonemanagement.entity.user.Gender;
import study.phonemanagement.entity.user.Role;
import study.phonemanagement.entity.user.User;
import study.phonemanagement.repository.UserRepository;

class CustomUserDetailsServiceTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @DisplayName("username으로 유저를 찾으면 CustomUserDetails를 반환한다")
    @Test
    void loadUserByUsername() {
        // given
        User user = User.builder()
                .username("testUser")
                .password("testPassword")
                .gender(Gender.MALE)
                .role(Role.USER)
                .email("test@test.com")
                .build();

        userRepository.save(user);

        // when
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getUsername());

        // then
        Assertions.assertThat(userDetails.getUsername()).isEqualTo(user.getUsername());
        Assertions.assertThat(userDetails.getPassword()).isEqualTo(user.getPassword());
    }

    @DisplayName("username에 해당하는 유저가 없으면 예외가 발생한다")
    @Test
    void loadUserByUsernameNotFoundUser() {
        // given - when - then
        Assertions.assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername("testUser"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("해당 유저를 찾을 수 없습니다.");
    }
}
