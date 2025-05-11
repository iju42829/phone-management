package study.phonemanagement.mapper.cart;


import study.phonemanagement.entity.cart.Cart;
import study.phonemanagement.entity.phone.Phone;
import study.phonemanagement.entity.user.User;
import study.phonemanagement.service.cart.response.CartResponse;

public interface CartMapper {
    Cart toCart(Phone phone, User user, Integer quantity);
    CartResponse toCartResponse(Cart cart);
}
