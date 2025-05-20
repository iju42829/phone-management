package study.phonemanagement.service.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static study.phonemanagement.entity.user.Role.USER;

class CustomUserDetailsTest {

    @DisplayName("CustomUserDetails 생성 시 필드가 정상적으로 주입된다")
    @Test
    void CustomUserDetailsField() {
        // given - when
        CustomUserDetails customUserDetails = new CustomUserDetails("testUser", "testPassword", USER.name());

        // then
        assertThat(customUserDetails.getUsername()).isEqualTo("testUser");
        assertThat(customUserDetails.getPassword()).isEqualTo("testPassword");
        assertThat(customUserDetails.getAuthorities().iterator().next().getAuthority()).isEqualTo(USER.name());
    }
}
