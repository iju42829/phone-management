package study.phonemanagement.entity.user;

import jakarta.persistence.*;
import lombok.*;
import study.phonemanagement.entity.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(unique = true)
    private String email;

    @Builder
    public User(String username, String password, Gender gender, Role role, String email) {
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.role = role;
        this.email = email;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeRole(Role role) {
        this.role = role;
    }
}
