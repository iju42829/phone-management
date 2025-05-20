package study.phonemanagement.entity.phone;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import study.phonemanagement.exception.phone.PhoneStockShortageException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static study.phonemanagement.common.ErrorCode.PHONE_STOCK_SHORTAGE;
import static study.phonemanagement.entity.phone.Manufacturer.APPLE;
import static study.phonemanagement.entity.phone.Manufacturer.SAMSUNG;
import static study.phonemanagement.entity.phone.Status.AVAILABLE;
import static study.phonemanagement.entity.phone.Status.OUT_OF_STOCK;
import static study.phonemanagement.entity.phone.Storage.STORAGE_128;
import static study.phonemanagement.entity.phone.Storage.STORAGE_256;

class PhoneTest {

    @DisplayName("휴대폰 수정 시 파라미터에 있는 값을 기준으로 수정됩니다.")
    @Test
    void update() {
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
        phone.update(
                "testPhone2",
                APPLE,
                STORAGE_256,
                OUT_OF_STOCK,
                200000,
                5,
                "testColor2"
        );

        // then
        assertThat(phone.getName()).isEqualTo("testPhone2");
        assertThat(phone.getManufacturer()).isEqualTo(APPLE);
        assertThat(phone.getStorage()).isEqualTo(STORAGE_256);
        assertThat(phone.getStatus()).isEqualTo(OUT_OF_STOCK);
        assertThat(phone.getPrice()).isEqualTo(200000);
        assertThat(phone.getQuantity()).isEqualTo(5);
        assertThat(phone.getColor()).isEqualTo("testColor2");
    }

    @DisplayName("휴대폰 삭제시 deleteAt에 현재 시간이 등록됩니다.")
    @Test
    void delete() {
        // given
        Phone phone = new Phone();

        // when
        phone.delete();

        // then
        assertThat(phone.getDeletedAt()).isNotNull();
    }

    @DisplayName("수량 감소 로직 실행 시, 현재 수량에서 전달된 수량만큼 차감된다")
    @Test
    void reduceQuantity() {
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
        phone.reduceQuantity(3);

        // then
        assertThat(phone.getQuantity()).isEqualTo(7);
    }

    @DisplayName("재고 수량이 0이 되면 상태를 품절(OUT_OF_STOCK)로 변경한다")
    @Test
    void reduceQuantitySetsStatusToOutOfStockWhenQuantityBecomesZero() {
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
        phone.reduceQuantity(10);

        // then
        assertThat(phone.getQuantity()).isEqualTo(0);
        assertThat(phone.getStatus()).isEqualTo(OUT_OF_STOCK);
    }
    @DisplayName("요청 수량이 재고보다 많으면 PhoneStockShortageException이 발생한다")
    @Test
    void reduceQuantityThrowsExceptionWhenRequestedQuantityExceedsStock() {
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

        // when - then
        assertThatThrownBy(() -> phone.reduceQuantity(11))
                .isInstanceOf(PhoneStockShortageException.class)
                .hasMessage(PHONE_STOCK_SHORTAGE.getMessage());
    }

    @DisplayName("수량 증가 로직 실행 시, 현재 수량에서 전달된 수량만큼 증가된다")
    @Test
    void addQuantity() {
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
        phone.addQuantity(3);

        // then
        assertThat(phone.getQuantity()).isEqualTo(13);
    }
}
