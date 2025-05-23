package study.phonemanagement.service.cart;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import study.phonemanagement.IntegrationTestSupport;
import study.phonemanagement.controller.cart.request.CreateCartOrderPhoneRequest;
import study.phonemanagement.controller.cart.request.CreateCartOrderRequest;
import study.phonemanagement.controller.cart.request.CreateCartRequest;
import study.phonemanagement.entity.cart.Cart;
import study.phonemanagement.entity.common.Address;
import study.phonemanagement.entity.phone.Manufacturer;
import study.phonemanagement.entity.phone.Phone;
import study.phonemanagement.entity.user.Gender;
import study.phonemanagement.entity.user.Role;
import study.phonemanagement.entity.user.User;
import study.phonemanagement.exception.cart.CartNotFoundException;
import study.phonemanagement.exception.phone.PhoneNotFoundException;
import study.phonemanagement.exception.user.UserNotFoundException;
import study.phonemanagement.repository.UserRepository;
import study.phonemanagement.repository.order.CartRepository;
import study.phonemanagement.repository.phone.PhoneRepository;
import study.phonemanagement.service.cart.response.CartResponse;
import study.phonemanagement.service.user.CustomUserDetails;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static study.phonemanagement.common.ErrorCode.*;
import static study.phonemanagement.entity.phone.Status.AVAILABLE;
import static study.phonemanagement.entity.phone.Storage.STORAGE_128;

class CartServiceTest extends IntegrationTestSupport {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PhoneRepository phoneRepository;

    @AfterEach
    void tearDown() {
        cartRepository.deleteAllInBatch();
        phoneRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("장바구니 생성에 성공하면 장바구니 ID를 반환한다.")
    @Test
    void createCart() {
        // given
        Phone phone = createTestPhoneEntity("testPhone", Manufacturer.SAMSUNG);
        User user = createTestUser("testUser", "test", "test@test", Gender.MALE, Role.USER);

        phoneRepository.save(phone);
        userRepository.save(user);

        CreateCartRequest createCartRequest = new CreateCartRequest();
        createCartRequest.setCount(1);
        createCartRequest.setPhoneId(phone.getId());

        CustomUserDetails customUserDetails = new CustomUserDetails(user.getUsername(), user.getPassword(), user.getRole().getKey());

        // when
        Long cartId = cartService.createCart(createCartRequest, customUserDetails);

        // then
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new CartNotFoundException(CART_NOT_FOUND));

        assertThat(cart.getCount()).isEqualTo(createCartRequest.getCount());
        assertThat(cart.getPhone()).isNotNull();
        assertThat(cart.getUser()).isNotNull();
    }

    @DisplayName("존재하지 않는 사용자로 장바구니를 생성하면 예외가 발생한다.")
    @Test
    void createCartWithInvalidUser() {
        // given
        CustomUserDetails customUserDetails = new CustomUserDetails("testUser", "test", Role.USER.getKey());
        CreateCartRequest createCartRequest = new CreateCartRequest();
        createCartRequest.setCount(1);
        createCartRequest.setPhoneId(1L);

        // when - then
        assertThatThrownBy(() -> cartService.createCart(createCartRequest, customUserDetails))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage(USER_NOT_FOUND.getMessage());
    }

    @DisplayName("존재하지 않는 휴대폰으로 장바구니를 생성하면 예외가 발생한다.")
    @Test
    void createCartWithInvalidPhone() {
        // given
        User user = createTestUser("testUser", "test", "test@test", Gender.MALE, Role.USER);
        userRepository.save(user);

        CustomUserDetails customUserDetails = new CustomUserDetails("testUser", "test", Role.USER.getKey());
        CreateCartRequest createCartRequest = new CreateCartRequest();
        createCartRequest.setCount(1);
        createCartRequest.setPhoneId(1L);

        // when - then
        assertThatThrownBy(() -> cartService.createCart(createCartRequest, customUserDetails))
                .isInstanceOf(PhoneNotFoundException.class)
                .hasMessage(PHONE_NOT_FOUND.getMessage());
    }

    @DisplayName("장바구니 항목을 삭제하면 해당 항목은 더 이상 조회되지 않는다.")
    @Test
    void removeCartItem() {
        // given
        Phone phone = createTestPhoneEntity("testPhone", Manufacturer.SAMSUNG);
        User user = createTestUser("testUser", "test", "test@test", Gender.MALE, Role.USER);

        phoneRepository.save(phone);
        userRepository.save(user);

        CreateCartRequest createCartRequest = new CreateCartRequest();
        createCartRequest.setCount(1);
        createCartRequest.setPhoneId(phone.getId());

        CustomUserDetails customUserDetails = new CustomUserDetails(user.getUsername(), user.getPassword(), user.getRole().getKey());
        Long cartId = cartService.createCart(createCartRequest, customUserDetails);

        // when
        cartService.removeCartItem(cartId);

        // then
        assertThatThrownBy(() -> cartRepository.findById(cartId).orElseThrow(() -> new CartNotFoundException(CART_NOT_FOUND)))
                .isInstanceOf(CartNotFoundException.class)
                .hasMessage(CART_NOT_FOUND.getMessage());
    }

    @DisplayName("존재하지 않는 사용자로 장바구니 목록을 조회하면 예외가 발생한다.")
    @Test
    void getCartListWithInvalidUser() {
        // given
        CustomUserDetails customUserDetails = new CustomUserDetails("testUser", "test", Role.USER.getKey());

        // when - then
        assertThatThrownBy(() -> cartService.getCartList(customUserDetails))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage(USER_NOT_FOUND.getMessage());
    }

    @DisplayName("사용자의 장바구니 목록을 조회하면 담긴 항목들이 반환된다.")
    @Test
    void getCartList() {
        // given
        Phone phone = createTestPhoneEntity("testPhone", Manufacturer.SAMSUNG);
        User user = createTestUser("testUser", "test", "test@test", Gender.MALE, Role.USER);

        phoneRepository.save(phone);
        userRepository.save(user);

        CreateCartRequest createCartRequest = new CreateCartRequest();
        createCartRequest.setCount(1);
        createCartRequest.setPhoneId(phone.getId());

        CustomUserDetails customUserDetails = new CustomUserDetails(user.getUsername(), user.getPassword(), user.getRole().getKey());
        Long cartId = cartService.createCart(createCartRequest, customUserDetails);

        // when
        List<CartResponse> cartList = cartService.getCartList(customUserDetails);

        // then
        assertThat(cartList).hasSize(1);
    }

    @DisplayName("주문 요청에 포함된 장바구니 항목들을 삭제한다.")
    @Test
    void clearCartAfterOrder() {
        // given
        Phone phone = createTestPhoneEntity("testPhone", Manufacturer.SAMSUNG);
        User user = createTestUser("testUser", "test", "test@test", Gender.MALE, Role.USER);

        phoneRepository.save(phone);
        userRepository.save(user);

        CreateCartRequest createCartRequest = new CreateCartRequest();
        createCartRequest.setCount(1);
        createCartRequest.setPhoneId(phone.getId());

        CustomUserDetails customUserDetails = new CustomUserDetails(user.getUsername(), user.getPassword(), user.getRole().getKey());
        Long cartId = cartService.createCart(createCartRequest, customUserDetails);

        CreateCartOrderPhoneRequest createCartOrderPhoneRequest = new CreateCartOrderPhoneRequest(cartId, phone.getId(), 1);
        CreateCartOrderRequest createCartOrderRequest = new CreateCartOrderRequest();
        createCartOrderRequest.setCreateCartOrderPhoneRequests(List.of(createCartOrderPhoneRequest));

        // when
        cartService.clearCartAfterOrder(createCartOrderRequest);

        // then
        assertThatThrownBy(() -> cartRepository.findById(cartId).orElseThrow(() -> new CartNotFoundException(CART_NOT_FOUND)))
                .isInstanceOf(CartNotFoundException.class)
                .hasMessage(CART_NOT_FOUND.getMessage());
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
