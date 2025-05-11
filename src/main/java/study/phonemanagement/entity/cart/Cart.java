package study.phonemanagement.entity.cart;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.phonemanagement.entity.BaseEntity;
import study.phonemanagement.entity.phone.Phone;
import study.phonemanagement.entity.user.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Phone phone;

    @Column(nullable = false)
    private Integer quantity;

    @Builder
    private Cart(User user, Phone phone, Integer quantity) {
        this.user = user;
        this.phone = phone;
        this.quantity = quantity;
    }
}
