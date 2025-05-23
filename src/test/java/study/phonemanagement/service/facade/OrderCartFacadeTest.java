package study.phonemanagement.service.facade;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import study.phonemanagement.service.cart.CartService;
import study.phonemanagement.service.order.OrderService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderCartFacadeTest {

    @Mock
    private OrderService orderService;

    @Mock
    private CartService cartService;

    @InjectMocks
    private OrderCartFacade orderCartFacade;

    @DisplayName("장바구니 주문에 성공하면 주문 ID를 반환한다.")
    @Test
    void orderAndClearCart() {
        when(orderService.createOrderByCart(any(), any()))
                .thenReturn(1L);

        doNothing().when(cartService)
                .clearCartAfterOrder(any());

        Long result = orderCartFacade.orderAndClearCart(any(), any());

        assertThat(result).isEqualTo(1L);
    }
}
