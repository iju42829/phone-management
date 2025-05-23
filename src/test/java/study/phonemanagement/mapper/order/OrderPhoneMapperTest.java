package study.phonemanagement.mapper.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import study.phonemanagement.IntegrationTestSupport;
import study.phonemanagement.service.order.response.OrderPhoneDetailResponse;

import static org.assertj.core.api.Assertions.*;


class OrderPhoneMapperTest extends IntegrationTestSupport {

    @Autowired
    private OrderPhoneMapper orderPhoneMapper;

    @DisplayName("orderPhone 이 null 이면 null을 반환한다.")
    @Test
    void toOrderPhoneDetailResponseWithNull() {
        // given - when
        OrderPhoneDetailResponse orderPhoneDetailResponse = orderPhoneMapper.toOrderPhoneDetailResponse(null);

        // then
        assertThat(orderPhoneDetailResponse).isNull();
    }
}
