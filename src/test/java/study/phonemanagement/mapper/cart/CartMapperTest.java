package study.phonemanagement.mapper.cart;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import study.phonemanagement.IntegrationTestSupport;
import study.phonemanagement.entity.cart.Cart;
import study.phonemanagement.entity.common.Address;
import study.phonemanagement.entity.phone.Manufacturer;
import study.phonemanagement.entity.phone.Phone;
import study.phonemanagement.entity.user.Gender;
import study.phonemanagement.entity.user.Role;
import study.phonemanagement.entity.user.User;
import study.phonemanagement.service.cart.response.CartResponse;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static study.phonemanagement.entity.phone.Manufacturer.SAMSUNG;
import static study.phonemanagement.entity.phone.Status.AVAILABLE;
import static study.phonemanagement.entity.phone.Storage.STORAGE_128;
import static study.phonemanagement.entity.user.Gender.MALE;
import static study.phonemanagement.entity.user.Role.USER;

class CartMapperTest extends IntegrationTestSupport {

    @Autowired
    private CartMapper cartMapper;

    @DisplayName("phone, user, 수량을 Cart로 변환한다.")
    @Test
    void toCart() {
        // given
        User user = createTestUser("testUser", "test", "test@test", MALE, USER);
        Phone phone = createTestPhoneEntity("testPhone", SAMSUNG);

        // when
        Cart cart = cartMapper.toCart(phone, user, 3);


        // then
        assertThat(cart.getCount()).isEqualTo(3);
        assertThat(cart.getUser()).isNotNull();
        assertThat(cart.getPhone()).isNotNull();
        assertThat(cart.getId()).isNull();
    }

    private static Stream<Arguments> provideNullInputsForToCart() {
        // given
        User user = createTestUser("testUser", "test", "test@test", MALE, USER);
        Phone phone = createTestPhoneEntity("testPhone", SAMSUNG);

        return Stream.of(
                Arguments.of(null, user, 1),
                Arguments.of(phone, null, 1),
                Arguments.of(null, null, 1)
        );
    }

    @MethodSource("provideNullInputsForToCart")
    @DisplayName("phone, user 중 하나라도 null이면 null을 반환한다.")
    @ParameterizedTest
    void toCartWithAnyNull(Phone phone, User user, Integer quantity) {
        // when
        Cart cart = cartMapper.toCart(phone, user, quantity);

        // then
        assertThat(cart).isNull();
    }

    @DisplayName("Cart를 CartResponse로 변환한다.")
    @Test
    void toCartResponse() {
        // given
        User user = createTestUser("testUser", "test", "test@test", MALE, USER);
        Phone phone = createTestPhoneEntity("testPhone", SAMSUNG);

        Cart cart = Cart.builder()
                .user(user)
                .phone(phone)
                .count(1)
                .build();

        // when
        CartResponse cartResponse = cartMapper.toCartResponse(cart);

        // then
        Assertions.assertThat(cartResponse)
                .extracting(CartResponse::getName, CartResponse::getManufacturer, CartResponse::getStorage, CartResponse::getPrice, CartResponse::getCount)
                .containsExactly(phone.getName(), phone.getManufacturer().name(), phone.getStorage().name(), phone.getPrice(), cart.getCount());
    }

    @DisplayName("cart가 null이면 null을 반환한다.")
    @Test
    void toCartResponseWithNull() {
        // given - when
        CartResponse cartResponse = cartMapper.toCartResponse(null);

        // then
        assertThat(cartResponse).isNull();
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
