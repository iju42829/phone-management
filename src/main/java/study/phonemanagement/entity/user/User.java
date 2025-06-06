package study.phonemanagement.entity.user;

import jakarta.persistence.*;
import lombok.*;
import study.phonemanagement.entity.BaseTimeEntity;
import study.phonemanagement.entity.common.Address;
import study.phonemanagement.entity.order.Order;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(unique = true, nullable = false)
    private String email;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    @Builder
    private User(String username, String password, Gender gender, Role role, String email, Address address) {
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.role = role;
        this.email = email;
        this.address = address;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeRole(Role role) {
        this.role = role;
    }
}
