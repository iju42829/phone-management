package study.phonemanagement.entity.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import study.phonemanagement.entity.common.Address;

import static org.assertj.core.api.Assertions.*;

class DeliveryTest {

    @DisplayName("도시, 도로명, 우편번호, 상세주소로 Delivery를 생성한다")
    @Test
    void createDelivery() {
        // given - when
        Delivery delivery = Delivery.createDelivery("testCity", "testStreet", "testZipcode", "testDetail");

        // then
        assertThat(delivery.getAddress())
                .extracting(Address::getCity, Address::getStreet, Address::getZipcode, Address::getDetail)
                .containsExactly("testCity", "testStreet", "testZipcode", "testDetail");
    }
}
