package study.phonemanagement.entity.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import study.phonemanagement.entity.phone.Phone;

import static org.assertj.core.api.Assertions.*;
import static study.phonemanagement.entity.phone.Manufacturer.SAMSUNG;
import static study.phonemanagement.entity.phone.Status.AVAILABLE;
import static study.phonemanagement.entity.phone.Storage.STORAGE_128;

class OrderPhoneTest {

    @DisplayName("createOrderPhone 실행 시 휴대폰 수량이 감소하고 주문 정보가 생성된다")
    @Test
    void createOrderPhone() {
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

        // when
        OrderPhone orderPhone = OrderPhone.createOrderPhone(phone, 3, phone.getPrice());

        // then
        assertThat(orderPhone.getPhone().getQuantity()).isEqualTo(7);

        assertThat(orderPhone).extracting(OrderPhone::getPhone, OrderPhone::getOrderPrice, OrderPhone::getCount)
                .containsExactly(phone, phone.getPrice(), 3);
    }

    @DisplayName("cancel 실행 시 휴대폰 수량이 주문전으로 복구된다")
    @Test
    void cancel() {
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

        // when
        orderPhone.cancel();

        // then
        assertThat(orderPhone.getPhone().getQuantity()).isEqualTo(10);
    }
}
