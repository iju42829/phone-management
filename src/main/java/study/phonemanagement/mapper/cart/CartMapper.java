package study.phonemanagement.mapper.cart;


import study.phonemanagement.entity.cart.Cart;
import study.phonemanagement.entity.phone.Phone;
import study.phonemanagement.entity.user.User;

public interface CartMapper {
    Cart toCart(Phone phone, User user, Integer quantity);
}
