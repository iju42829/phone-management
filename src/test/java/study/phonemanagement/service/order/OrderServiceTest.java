package study.phonemanagement.service.order;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import study.phonemanagement.IntegrationTestSupport;
import study.phonemanagement.controller.order.request.CreateOrderDeliveryRequest;
import study.phonemanagement.controller.order.request.CreateOrderPhoneRequest;
import study.phonemanagement.controller.order.request.CreateOrderRequest;
import study.phonemanagement.entity.common.Address;
import study.phonemanagement.entity.order.Order;
import study.phonemanagement.entity.order.OrderPhone;
import study.phonemanagement.entity.phone.Manufacturer;
import study.phonemanagement.entity.phone.Phone;
import study.phonemanagement.entity.user.Gender;
import study.phonemanagement.entity.user.Role;
import study.phonemanagement.entity.user.User;
import study.phonemanagement.exception.order.OrderNotFoundException;
import study.phonemanagement.exception.order.OrderPhoneNotFoundException;
import study.phonemanagement.exception.phone.PhoneNotFoundException;
import study.phonemanagement.repository.DeliveryRepository;
import study.phonemanagement.repository.order.OrderPhoneRepository;
import study.phonemanagement.repository.order.OrderRepository;
import study.phonemanagement.repository.UserRepository;
import study.phonemanagement.repository.phone.PhoneRepository;
import study.phonemanagement.service.user.CustomUserDetails;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static study.phonemanagement.common.ErrorCode.*;
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

    private CreateOrderRequest createOrderRequestTest(Phone phone) {
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
