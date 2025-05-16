package study.phonemanagement.entity.order;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.phonemanagement.entity.BaseEntity;
import study.phonemanagement.entity.common.Address;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "delivery")
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    public static Delivery createDelivery(String city, String street, String zipcode, String detail) {
        Delivery delivery = new Delivery();
        delivery.setAddress(new Address(city, street, zipcode, detail));
        delivery.setStatus(DeliveryStatus.READY);

        return delivery;
    }

    public void changeStatus(DeliveryStatus status) {
        this.status = status;
    }
}
