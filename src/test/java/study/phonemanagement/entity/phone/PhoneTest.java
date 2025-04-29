package study.phonemanagement.entity.phone;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
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
}
