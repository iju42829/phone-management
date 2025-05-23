package study.phonemanagement.service.order;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import study.phonemanagement.IntegrationTestSupport;
import study.phonemanagement.controller.order.request.CancelOrderRequest;
import study.phonemanagement.controller.order.request.CreateOrderDeliveryRequest;
import study.phonemanagement.controller.order.request.CreateOrderPhoneRequest;
import study.phonemanagement.controller.order.request.CreateOrderRequest;
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
import study.phonemanagement.exception.order.OrderCancelForbiddenException;
import study.phonemanagement.exception.order.OrderCannotBeCancelledException;
import study.phonemanagement.exception.order.OrderNotFoundException;
import study.phonemanagement.exception.order.OrderPhoneNotFoundException;
import study.phonemanagement.exception.phone.PhoneNotFoundException;
import study.phonemanagement.exception.user.UserNotFoundException;
import study.phonemanagement.repository.DeliveryRepository;
import study.phonemanagement.repository.UserRepository;
import study.phonemanagement.repository.order.OrderPhoneRepository;
import study.phonemanagement.repository.order.OrderRepository;
import study.phonemanagement.repository.phone.PhoneRepository;
import study.phonemanagement.service.order.response.OrderListResponse;
import study.phonemanagement.service.user.CustomUserDetails;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static study.phonemanagement.common.ErrorCode.*;
import static study.phonemanagement.entity.order.OrderStatus.CANCEL;
import static study.phonemanagement.entity.phone.Manufacturer.SAMSUNG;
import static study.phonemanagement.entity.phone.Status.AVAILABLE;
import static study.phonemanagement.entity.phone.Storage.STORAGE_128;
import static study.phonemanagement.entity.user.Gender.MALE;
import static study.phonemanagement.entity.user.Role.USER;

class OrderServiceTest extends IntegrationTestSupport {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private OrderPhoneRepository orderPhoneRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @AfterEach
    void tearDown() {
        orderPhoneRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        phoneRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        deliveryRepository.deleteAllInBatch();
    }

    @Transactional
    @DisplayName("주문 생성 시 휴대폰 주문 정보가 저장된다.")
    @Test
    void createOrderCheckOrderPhone() {
        // given
        Phone phone = createTestPhoneEntity("testPhone", SAMSUNG);
        User user = createTestUser("testUser", "test", "test@test", MALE, USER);

        phoneRepository.save(phone);
        userRepository.save(user);

        CustomUserDetails customUserDetails = new CustomUserDetails(user.getUsername(), user.getPassword(), user.getRole().getKey());

        // when
        Long orderSavedId = orderService.createOrder(createOrderRequestTest(phone), customUserDetails);

        // then
        Order order = orderRepository.findWithDeliveryById(orderSavedId).orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND));
        OrderPhone orderPhone = orderPhoneRepository.findByOrderId(orderSavedId).orElseThrow(() -> new OrderPhoneNotFoundException(ORDER_PHONE_NOT_FOUND));

        assertThat(orderPhone)
                .extracting(OrderPhone::getCount, OrderPhone::getOrderPrice)
                .containsExactly(1, phone.getPrice());
    }

    @Transactional
    @DisplayName("주문 생성 시 배송지 정보가 저장된다.")
    @Test
    void createOrderCheckDelivery() {
        // given
        Phone phone = createTestPhoneEntity("testPhone", SAMSUNG);
        User user = createTestUser("testUser", "test", "test@test", MALE, USER);

        phoneRepository.save(phone);
        userRepository.save(user);

        CustomUserDetails customUserDetails = new CustomUserDetails(user.getUsername(), user.getPassword(), user.getRole().getKey());

        // when
        Long orderSavedId = orderService.createOrder(createOrderRequestTest(phone), customUserDetails);

        // then
        Order order = orderRepository.findWithDeliveryById(orderSavedId).orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND));

        assertThat(order.getDelivery().getAddress())
                .extracting(Address::getCity, Address::getStreet, Address::getZipcode, Address::getDetail)
                .containsExactly("testCity", "testStreet", "testZipcode", "testDetail");

    }

    @DisplayName("주문 성공 시 재고가 주문 수량만큼 감소한다.")
    @Test
    void createOrderCheckPhoneQuantity() {
        // given
        Phone phone = createTestPhoneEntity("testPhone", SAMSUNG);
        User user = createTestUser("testUser", "test", "test@test", MALE, USER);

        phoneRepository.save(phone);
        userRepository.save(user);

        CustomUserDetails customUserDetails = new CustomUserDetails(user.getUsername(), user.getPassword(), user.getRole().getKey());

        // when
        Long orderSavedId = orderService.createOrder(createOrderRequestTest(phone), customUserDetails);

        // then
        Phone checkPhone = phoneRepository.findById(phone.getId()).orElseThrow(() -> new PhoneNotFoundException(PHONE_NOT_FOUND));
        assertThat(checkPhone.getQuantity()).isEqualTo(99);
    }

    @DisplayName("동시 주문 요청 시 재고가 정상적으로 감소하는지 검증한다.")
    @Test
    void concurrencyCreateOrder() throws InterruptedException {
        // given
        Phone phone = createTestPhoneEntity("testPhone", SAMSUNG);
        User user = createTestUser("testUser", "test", "test@test", MALE, USER);

        phoneRepository.save(phone);
        userRepository.save(user);

        CustomUserDetails customUserDetails = new CustomUserDetails(user.getUsername(), user.getPassword(), user.getRole().getKey());

        // when
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    orderService.createOrder(createOrderRequestTest(phone), customUserDetails);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // then
        Phone checkPhone = phoneRepository.findById(phone.getId()).orElseThrow(() -> new PhoneNotFoundException(PHONE_NOT_FOUND));
        assertThat(checkPhone.getQuantity()).isEqualTo(0);
    }

    @DisplayName("유저의 주문 목록을 페이징 조회한다.")
    @Test
    void getOrders() {
        // given
        Phone phone = createTestPhoneEntity("testPhone", SAMSUNG);
        User user = createTestUser("testUser", "test", "test@test", MALE, USER);

        userRepository.save(user);
        phoneRepository.save(phone);

        OrderPhone orderPhone = OrderPhone.createOrderPhone(phone, 1, phone.getPrice());
        Delivery delivery = Delivery.createDelivery(user.getAddress().getCity(), user.getAddress().getStreet(), user.getAddress().getZipcode(), user.getAddress().getDetail());

        Order order = Order.createOrder(user, delivery, List.of(orderPhone));
        orderRepository.save(order);

        CustomUserDetails customUserDetails = new CustomUserDetails(user.getUsername(), user.getPassword(), user.getRole().getKey());

        // when
        Page<OrderListResponse> orders = orderService.getOrders(customUserDetails, 1, 10);

        // then
        assertThat(orders.getTotalElements()).isEqualTo(1);
    }

    @DisplayName("주어진 사용자명으로 주문 목록을 페이징 조회한다.")
    @Test
    void getOrdersByUsername() {
        // given
        Phone phone = createTestPhoneEntity("testPhone", SAMSUNG);
        User user = createTestUser("testUser", "test", "test@test", MALE, USER);

        userRepository.save(user);
        phoneRepository.save(phone);

        OrderPhone orderPhone = OrderPhone.createOrderPhone(phone, 1, phone.getPrice());
        Delivery delivery = Delivery.createDelivery(user.getAddress().getCity(), user.getAddress().getStreet(), user.getAddress().getZipcode(), user.getAddress().getDetail());

        Order order = Order.createOrder(user, delivery, List.of(orderPhone));
        orderRepository.save(order);

        CustomUserDetails customUserDetails = new CustomUserDetails(user.getUsername(), user.getPassword(), user.getRole().getKey());

        // when
        Page<OrderListResponse> orders = orderService.getOrdersByUsername(user.getUsername(), 1, 10);

        // then
        assertThat(orders.getTotalElements()).isEqualTo(1);
    }

    @DisplayName("정상적인 주문을 취소하면 상태가 CANCEL로 변경된다.")
    @Test
    void cancelOrder() {
        // given
        Phone phone = createTestPhoneEntity("testPhone", SAMSUNG);
        User user = createTestUser("testUser", "test", "test@test", MALE, USER);

        userRepository.save(user);
        phoneRepository.save(phone);

        OrderPhone orderPhone = OrderPhone.createOrderPhone(phone, 1, phone.getPrice());
        Delivery delivery = Delivery.createDelivery(user.getAddress().getCity(), user.getAddress().getStreet(), user.getAddress().getZipcode(), user.getAddress().getDetail());

        Order order = Order.createOrder(user, delivery, List.of(orderPhone));
        orderRepository.save(order);

        CustomUserDetails customUserDetails = new CustomUserDetails(user.getUsername(), user.getPassword(), user.getRole().getKey());
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest();
        cancelOrderRequest.setOrderId(order.getId());

        // when
        orderService.cancelOrder(cancelOrderRequest, customUserDetails);

        // then
        Order findOrder = orderRepository.findById(order.getId()).orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND));
        assertThat(findOrder.getStatus()).isEqualTo(CANCEL);
    }

    @DisplayName("존재하지 않는 사용자로 주문 취소 시 예외가 발생한다.")
    @Test
    void cancelOrderWithInvalidUser() {
        // given
        CustomUserDetails customUserDetails = new CustomUserDetails("", "", "");
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest();
        cancelOrderRequest.setOrderId(1L);

        // when - then
        assertThatThrownBy(() -> orderService.cancelOrder(cancelOrderRequest, customUserDetails))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage(USER_NOT_FOUND.getMessage());
    }

    @DisplayName("존재하지 않는 주문 ID로 취소 시 예외가 발생한다.")
    @Test
    void cancelOrderWithInvalidOrder() {
        // given
        User user = createTestUser("testUser", "test", "test@test", MALE, USER);
        userRepository.save(user);

        CustomUserDetails customUserDetails = new CustomUserDetails(user.getUsername(), user.getPassword(), user.getRole().getKey());
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest();
        cancelOrderRequest.setOrderId(1L);

        // when - then
        assertThatThrownBy(() -> orderService.cancelOrder(cancelOrderRequest, customUserDetails))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessage(ORDER_NOT_FOUND.getMessage());
    }

    @DisplayName("배송 상태가 READY가 아니면 예외가 발생한다.")
    @Test
    void cancelOrderWithDeliveryStatusIsNotReady() {
        // given
        Phone phone = createTestPhoneEntity("testPhone", SAMSUNG);
        User user = createTestUser("testUser", "test", "test@test", MALE, USER);

        userRepository.save(user);
        phoneRepository.save(phone);

        OrderPhone orderPhone = OrderPhone.createOrderPhone(phone, 1, phone.getPrice());
        Delivery delivery = Delivery.createDelivery(user.getAddress().getCity(), user.getAddress().getStreet(), user.getAddress().getZipcode(), user.getAddress().getDetail());

        Order order = Order.createOrder(user, delivery, List.of(orderPhone));
        orderRepository.save(order);

        CustomUserDetails customUserDetails = new CustomUserDetails(user.getUsername(), user.getPassword(), user.getRole().getKey());
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest();
        cancelOrderRequest.setOrderId(order.getId());

        delivery.setStatus(DeliveryStatus.COMP);
        deliveryRepository.save(delivery);

        // when - then
        assertThatThrownBy(() -> orderService.cancelOrder(cancelOrderRequest, customUserDetails))
                .isInstanceOf(OrderCannotBeCancelledException.class)
                .hasMessage(ORDER_CANNOT_BE_CANCELLED.getMessage());
    }

    @DisplayName("주문 소유자도 아니고 관리자가 아닌 유저가 취소하면 예외가 발생한다.")
    @Test
    void cancelOrderByAnotherUser() {
        // given
        Phone phone = createTestPhoneEntity("testPhone", SAMSUNG);
        User user = createTestUser("testUser", "test", "test@test", MALE, USER);
        User anotherUser = createTestUser("anotherUser", "test", "test1@test", MALE, USER);

        userRepository.saveAll(List.of(user, anotherUser));
        phoneRepository.save(phone);

        OrderPhone orderPhone = OrderPhone.createOrderPhone(phone, 1, phone.getPrice());
        Delivery delivery = Delivery.createDelivery(anotherUser.getAddress().getCity(), anotherUser.getAddress().getStreet(), anotherUser.getAddress().getZipcode(), anotherUser.getAddress().getDetail());

        Order order = Order.createOrder(user, delivery, List.of(orderPhone));
        orderRepository.save(order);

        CustomUserDetails customUserDetails = new CustomUserDetails(anotherUser.getUsername(), anotherUser.getPassword(), anotherUser.getRole().getKey());
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest();
        cancelOrderRequest.setOrderId(order.getId());

        // when - then
        assertThatThrownBy(() -> orderService.cancelOrder(cancelOrderRequest, customUserDetails))
                .isInstanceOf(OrderCancelForbiddenException.class)
                .hasMessage(ORDER_CANCEL_FORBIDDEN.getMessage());
    }


    private static CreateOrderRequest createOrderRequestTest(Phone phone) {
        CreateOrderDeliveryRequest orderDeliveryRequest = CreateOrderDeliveryRequest.createOrderDeliveryRequest("testCity", "testStreet", "testZipcode", "testDetail");
        CreateOrderPhoneRequest createOrderPhoneRequest = new CreateOrderPhoneRequest();
        createOrderPhoneRequest.setPhoneId(phone.getId());
        createOrderPhoneRequest.setCount(1);

        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setDelivery(orderDeliveryRequest);
        createOrderRequest.setCreateOrderPhoneRequestList(List.of(createOrderPhoneRequest));

        return createOrderRequest;
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
