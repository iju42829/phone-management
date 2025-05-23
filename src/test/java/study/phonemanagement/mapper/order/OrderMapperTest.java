package study.phonemanagement.mapper.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
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
import study.phonemanagement.service.order.response.OrderListResponse;
import study.phonemanagement.service.order.response.OrderPhoneDetailResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static study.phonemanagement.entity.order.DeliveryStatus.READY;
import static study.phonemanagement.entity.order.OrderStatus.ORDER;
import static study.phonemanagement.entity.phone.Manufacturer.SAMSUNG;
import static study.phonemanagement.entity.phone.Status.AVAILABLE;
import static study.phonemanagement.entity.phone.Storage.STORAGE_128;
import static study.phonemanagement.entity.user.Gender.MALE;
import static study.phonemanagement.entity.user.Role.USER;

class OrderMapperTest extends IntegrationTestSupport {

    @Autowired
    private OrderMapper orderMapper;

    @DisplayName("order, orderPhoneDetailResponses를 OrderListResponse으로 변환한다.")
    @Test
    void toOrderListResponse() {
        // given
        Phone phone = createTestPhoneEntity("testPhone", SAMSUNG);
        User user = createTestUser("testUser", "test", "test@test", MALE, USER);
        Delivery delivery = Delivery.createDelivery(user.getAddress().getCity(), user.getAddress().getStreet(), user.getAddress().getZipcode(), user.getAddress().getDetail());
        List<OrderPhoneDetailResponse> orderPhoneDetailResponses = new ArrayList<>();

        OrderPhone orderPhone = OrderPhone.createOrderPhone(phone, 1, phone.getPrice());
        Order order = Order.createOrder(user, delivery, List.of(orderPhone));

        List<OrderPhoneDetailResponse> phoneDetailResponses = order.getOrderPhones().stream()
                .map(p -> OrderPhoneDetailResponse.builder()
                        .phoneId(p.getPhone().getId())
                        .name(p.getPhone().getName())
                        .manufacturer(p.getPhone().getManufacturer().name())
                        .count(p.getCount())
                        .orderPrice(p.getOrderPrice())
                        .build())
                .toList();
        // when
        OrderListResponse orderListResponse = orderMapper.toOrderListResponse(order, orderPhoneDetailResponses);

        // then
        Assertions.assertThat(orderListResponse)
                .extracting(OrderListResponse::getOrderId, OrderListResponse::getOrderedAt, OrderListResponse::getOrderStatus, OrderListResponse::getDeliveryStatus, OrderListResponse::getTotalAmount)
                .containsExactly(null, null, ORDER.name(), READY.name(), phone.getPrice());
    }

    private static Stream<Arguments> provideNullInputsForToOrder() {
        // given
        Phone phone = createTestPhoneEntity("testPhone", SAMSUNG);
        User user = createTestUser("testUser", "test", "test@test", MALE, USER);
        Delivery delivery = Delivery.createDelivery(user.getAddress().getCity(), user.getAddress().getStreet(), user.getAddress().getZipcode(), user.getAddress().getDetail());
        List<OrderPhoneDetailResponse> orderPhoneDetailResponses = new ArrayList<>();

        OrderPhone orderPhone = OrderPhone.createOrderPhone(phone, 1, phone.getPrice());
        Order order = Order.createOrder(user, delivery, List.of(orderPhone));

        return Stream.of(
                Arguments.of(order, null),
                Arguments.of(null, orderPhoneDetailResponses),
                Arguments.of(null, null)
        );
    }

    @MethodSource("provideNullInputsForToOrder")
    @DisplayName("order, orderPhoneDetailResponseList중 하나라도 null이면 null을 반환한다.")
    @ParameterizedTest
    void toOrderListResponseWithAnyNull(Order order, List<OrderPhoneDetailResponse> orderPhoneDetailResponseList) {
        // when
        OrderListResponse orderListResponse = orderMapper.toOrderListResponse(order, orderPhoneDetailResponseList);

        // then
        assertThat(orderListResponse).isNull();
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
