package study.phonemanagement.entity.order;

import jakarta.persistence.*;
import lombok.*;
import study.phonemanagement.entity.BaseEntity;
import study.phonemanagement.entity.user.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch =  FetchType.LAZY)
    private User user;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Delivery delivery;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<OrderPhone> orderPhones = new ArrayList<>();

    public static Order createOrder(User user, Delivery delivery, List<OrderPhone> orderPhoneList) {
        Order order = new Order();
        order.changeUser(user);

        for (OrderPhone orderPhone : orderPhoneList) {
            order.addOrderPhone(orderPhone);
        }

        order.changeStatus(OrderStatus.ORDER);
        order.changeDelivery(delivery);

        return order;
    }

    // 연관 관계 편의 메서드
    public void addOrderPhone(OrderPhone orderPhone) {
        orderPhone.setOrder(this);
        orderPhones.add(orderPhone);
    }

    public void changeDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    public void changeUser(User user) {
        this.user = user;
        user.getOrders().add(this);
    }

    public void changeStatus(OrderStatus status) {
        this.status = status;
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderPhone orderPhone : orderPhones) {
            totalPrice += orderPhone.getOrderPrice();
        }

        return totalPrice;
    }
}
