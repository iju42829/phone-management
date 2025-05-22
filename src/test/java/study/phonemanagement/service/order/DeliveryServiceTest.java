package study.phonemanagement.service.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import study.phonemanagement.IntegrationTestSupport;
import study.phonemanagement.controller.order.request.UpdateDeliveryStatusRequest;
import study.phonemanagement.entity.common.Address;
import study.phonemanagement.entity.order.Delivery;
import study.phonemanagement.entity.order.DeliveryStatus;
import study.phonemanagement.entity.order.Order;
import study.phonemanagement.entity.order.OrderPhone;
import study.phonemanagement.entity.phone.Manufacturer;
import study.phonemanagement.entity.phone.Phone;
import study.phonemanagement.entity.user.Gender;
import study.phonemanagement.entity.user.Role;
import study.phonemanagement.entity.user.User;
import study.phonemanagement.exception.order.OrderNotFoundException;
import study.phonemanagement.repository.UserRepository;
import study.phonemanagement.repository.order.OrderRepository;
import study.phonemanagement.repository.phone.PhoneRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static study.phonemanagement.common.ErrorCode.ORDER_NOT_FOUND;
import static study.phonemanagement.entity.order.DeliveryStatus.DELIVERED;
import static study.phonemanagement.entity.phone.Status.AVAILABLE;
import static study.phonemanagement.entity.phone.Storage.STORAGE_128;

class DeliveryServiceTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private DeliveryService deliveryService;

    @Transactional
    @DisplayName("주문의 배송 상태를 DELIVERED로 변경한다")
    @Test
    void changeDeliveryStatus() {
        // given
        Phone phone = createTestPhoneEntity("testPhone", Manufacturer.SAMSUNG);
        User user = createTestUser("testUser", "test", "test@test", Gender.MALE, Role.USER);

        phoneRepository.save(phone);
        userRepository.save(user);
        Delivery delivery = Delivery.createDelivery(
                user.getAddress().getCity(),
                user.getAddress().getStreet(),
                user.getAddress().getZipcode(),
                user.getAddress().getDetail());

        OrderPhone orderPhone = OrderPhone.createOrderPhone(phone, 1, phone.getPrice());
        Order order = Order.createOrder(user, delivery, List.of(orderPhone));

        orderRepository.save(order);

        UpdateDeliveryStatusRequest updateDeliveryStatusRequest = createTestUpdateDeliveryStatusRequest(order.getId(), DELIVERED);

        // when
        deliveryService.changeDeliveryStatus(updateDeliveryStatusRequest);

        // then
        Order resultOrder = orderRepository.findWithDeliveryById(order.getId())
                .orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND));

        assertThat(resultOrder.getDelivery().getStatus()).isEqualTo(DELIVERED);
    }

    @DisplayName("존재하지 않는 주문 ID로 배송 상태를 변경하려 하면 예외가 발생한다")
    @Test
    void changeDeliveryStatusWithNoOrder() {
        // given
        UpdateDeliveryStatusRequest updateDeliveryStatusRequest = createTestUpdateDeliveryStatusRequest(1L, DELIVERED);

        // when - then
        assertThatThrownBy(() -> deliveryService.changeDeliveryStatus(updateDeliveryStatusRequest))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessage(ORDER_NOT_FOUND.getMessage());
    }

    private UpdateDeliveryStatusRequest createTestUpdateDeliveryStatusRequest(Long orderId, DeliveryStatus deliveryStatus) {
        UpdateDeliveryStatusRequest updateDeliveryStatusRequest = new UpdateDeliveryStatusRequest();
        updateDeliveryStatusRequest.setOrderId(orderId);
        updateDeliveryStatusRequest.setDeliveryStatus(deliveryStatus);

        return updateDeliveryStatusRequest;
    }

    private Phone createTestPhoneEntity(String name, Manufacturer manufacturer) {
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

    private User createTestUser(String username, String password, String email, Gender gender, Role role) {
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
