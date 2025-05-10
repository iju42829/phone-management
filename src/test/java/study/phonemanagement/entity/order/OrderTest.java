package study.phonemanagement.entity.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import study.phonemanagement.entity.phone.Phone;
import study.phonemanagement.entity.user.User;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static study.phonemanagement.entity.phone.Manufacturer.SAMSUNG;
import static study.phonemanagement.entity.phone.Status.AVAILABLE;
import static study.phonemanagement.entity.phone.Storage.STORAGE_128;

class OrderTest {

    @DisplayName("createOrder를 통해 주문을 생성하고 연관관계를 설정한다.")
    @Test
    void createOrder() {
        // given
        User user = User.builder().build();
        Delivery delivery = Delivery.createDelivery("testCity", "testStreet", "testZipcode", "testDetail");

        Phone phone = Phone.builder()
                .name("testPhone")
                .manufacturer(SAMSUNG)
                .storage(STORAGE_128)
                .status(AVAILABLE)
                .price(10000)
                .quantity(10)
                .color("testColor")
                .build();

        OrderPhone orderPhone = OrderPhone.createOrderPhone(phone, 3, phone.getPrice());
        List<OrderPhone> orderPhones = List.of(orderPhone);

        // when
        Order order = Order.createOrder(user, delivery, orderPhones);

        // then
        assertThat(order)
                .extracting(Order::getUser, Order::getDelivery, o -> o.getOrderPhones().size(), Order::getStatus)
                .containsExactly(user, delivery, 1, OrderStatus.ORDER);

    }

    @DisplayName("changeStatus를 통해 주문상태를 변경할 수 있다.")
    @Test
    void changeStatus() {
        // given
        Order order = new Order();

        // when
        order.changeStatus(OrderStatus.CANCEL);

        // then
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCEL);
    }

    @DisplayName("getTotalPrice는 모든 주문 상품의 가격을 합산하여 반환한다")
    @Test
    void getTotalPrice() {
        // given
        Phone phone = Phone.builder()
                .name("testPhone")
                .manufacturer(SAMSUNG)
                .storage(STORAGE_128)
                .status(AVAILABLE)
                .price(10000)
                .quantity(10)
                .color("testColor")
                .build();

        OrderPhone orderPhone = OrderPhone.createOrderPhone(phone, 3, phone.getPrice());

        Order order = new Order();
        order.addOrderPhone(orderPhone);

        assertThat(order.getTotalPrice()).isEqualTo(phone.getPrice() * orderPhone.getCount());
    }

    @DisplayName("연관관계 편의 메서드 테스트 - OrderPhone")
    @Test
    void addOrderPhone() {
        // given
        Order order = new Order();
        Phone phone = Phone.builder()
                .name("testPhone")
                .manufacturer(SAMSUNG)
                .storage(STORAGE_128)
                .status(AVAILABLE)
                .price(10000)
                .quantity(10)
                .color("testColor")
                .build();

        OrderPhone orderPhone = OrderPhone.createOrderPhone(phone, 3, phone.getPrice());

        // when
        order.addOrderPhone(orderPhone);

        // then
        assertThat(order.getOrderPhones()).containsExactly(orderPhone);
        assertThat(orderPhone.getOrder()).isEqualTo(order);
    }

    @DisplayName("연관관계 편의 메서드 테스트 - Delivery")
    @Test
    void changeDelivery() {
        // given
        Order order = new Order();
        Delivery delivery = Delivery.createDelivery("testCity", "testStreet", "testZipcode", "testDetail");

        // when
        order.changeDelivery(delivery);

        // then
        assertThat(order.getDelivery()).isEqualTo(delivery);
        assertThat(delivery.getOrder()).isEqualTo(order);
    }

    @DisplayName("연관관계 편의 메서드 테스트 - User")
    @Test
    void changeUser() {
        // given
        User user = User.builder().build();
        Order order = new Order();

        // when
        order.changeUser(user);

        // then
        assertThat(order.getUser()).isEqualTo(user);
        assertThat(user.getOrders()).containsExactly(order);
    }
}
