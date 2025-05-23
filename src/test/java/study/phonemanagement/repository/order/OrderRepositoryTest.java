package study.phonemanagement.repository.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import study.phonemanagement.IntegrationTestSupport;
import study.phonemanagement.entity.common.Address;
import study.phonemanagement.entity.order.Delivery;
import study.phonemanagement.entity.order.Order;
import study.phonemanagement.entity.order.OrderPhone;
import study.phonemanagement.entity.phone.Manufacturer;
import study.phonemanagement.entity.phone.Phone;
import study.phonemanagement.entity.user.Gender;
import study.phonemanagement.entity.user.Role;
import study.phonemanagement.entity.user.User;
import study.phonemanagement.repository.UserRepository;
import study.phonemanagement.repository.phone.PhoneRepository;

import java.util.List;

import static study.phonemanagement.entity.phone.Manufacturer.SAMSUNG;
import static study.phonemanagement.entity.phone.Status.AVAILABLE;
import static study.phonemanagement.entity.phone.Storage.STORAGE_128;
import static study.phonemanagement.entity.user.Gender.MALE;
import static study.phonemanagement.entity.user.Role.USER;

class OrderRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private OrderPhoneRepository orderPhoneRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        orderPhoneRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        phoneRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("username으로 주문 목록을 페이징 조회한다.")
    @Test
    void findAllOrderByUsername() {
        // given
        Phone phone = createTestPhoneEntity("testPhone", SAMSUNG);
        User user = createTestUser("testUser", "test", "test@test", MALE, USER);
        phoneRepository.save(phone);
        userRepository.save(user);

        Delivery delivery = Delivery.createDelivery(user.getAddress().getCity(), user.getAddress().getStreet(), user.getAddress().getZipcode(), user.getAddress().getDetail());

        OrderPhone orderPhone = OrderPhone.createOrderPhone(phone, 1, phone.getPrice());
        Order order = Order.createOrder(user, delivery, List.of(orderPhone));

        orderRepository.save(order);

        // when
        Page<Order> page = orderRepository.findAllOrderByUsername(user.getUsername(), PageRequest.of(0, 10));

        // then
        Assertions.assertThat(page.getTotalElements()).isEqualTo(1);
    }

    @DisplayName("username이 null인 경우 전체 주문 목록을 페이징 조회한다.")
    @Test
    void findAllOrderByUsernameWithNull() {
        // given
        Phone phone = createTestPhoneEntity("testPhone", SAMSUNG);
        User user = createTestUser("testUser", "test", "test@test", MALE, USER);
        phoneRepository.save(phone);
        userRepository.save(user);

        Delivery delivery = Delivery.createDelivery(user.getAddress().getCity(), user.getAddress().getStreet(), user.getAddress().getZipcode(), user.getAddress().getDetail());

        OrderPhone orderPhone = OrderPhone.createOrderPhone(phone, 1, phone.getPrice());
        Order order = Order.createOrder(user, delivery, List.of(orderPhone));

        orderRepository.save(order);

        // when
        Page<Order> page = orderRepository.findAllOrderByUsername(null, PageRequest.of(0, 10));

        // then
        Assertions.assertThat(page.getTotalElements()).isEqualTo(1);
    }

    private static Phone createTestPhoneEntity(String name, Manufacturer manufacturer) {
        return Phone.builder()
                .name(name)
                .manufacturer(manufacturer)
                .storage(STORAGE_128)
                .status(AVAILABLE)
                .price(200000)
                .quantity(100)
                .color("testColor")
                .build();
    }

    private static User createTestUser(String username, String password, String email, Gender gender, Role role) {
        return User.builder()
                .username(username)
                .password(password)
                .gender(gender)
                .role(role)
                .email(email)
                .address(new Address("testCity", "testStreet", "testZipcode", "testDetail"))
                .build();
    }
}
