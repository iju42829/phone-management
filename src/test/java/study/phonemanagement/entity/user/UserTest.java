package study.phonemanagement.entity.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static study.phonemanagement.entity.user.Role.*;


class UserTest {

    @DisplayName("파라미터에 있는 비밀번호로 유저 비밀번호가 변경됩니다.")
    @Test
    void changePassword() {
        // given
        User user = User.builder()
                .password("test1")
                .build();

        // when
        user.changePassword("test2");

        // then
        assertThat(user.getPassword()).isEqualTo("test2");
    }

    @DisplayName("파라미터에 있는 권한으로 유저 권한이 변경됩니다.")
    @Test
    void changeRole() {
        // given
        User user = User.builder()
                .role(USER)
                .build();

        // when
        user.changeRole(ADMIN);

        // then
        assertThat(user.getRole()).isEqualTo(ADMIN);
    }
}
