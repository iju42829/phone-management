package study.phonemanagement.entity.order;

import jakarta.persistence.*;
import lombok.*;
import study.phonemanagement.entity.BaseEntity;
import study.phonemanagement.entity.phone.Phone;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderPhone extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Phone phone;

    private Integer count;
    private Integer orderPrice;

    public static OrderPhone createOrderPhone(Phone phone, Integer count, Integer orderPrice) {
        OrderPhone orderPhone = new OrderPhone();
        orderPhone.setPhone(phone);
        orderPhone.setCount(count);
        orderPhone.setOrderPrice(orderPrice);

        phone.reduceQuantity(count);

        return orderPhone;
    }

    public void cancel() {
        phone.addQuantity(count);
    }
}
