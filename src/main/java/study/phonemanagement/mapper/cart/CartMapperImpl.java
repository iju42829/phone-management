package study.phonemanagement.mapper.cart;

import org.springframework.stereotype.Component;
import study.phonemanagement.entity.cart.Cart;
import study.phonemanagement.entity.phone.Phone;
import study.phonemanagement.entity.user.User;

@Component
public class CartMapperImpl implements CartMapper{
    @Override
    public Cart toCart(Phone phone, User user, Integer quantity) {
        if (phone == null || user == null) {
            return null;
        }

        return Cart.builder()
                .user(user)
                .phone(phone)
                .quantity(quantity)
                .build();
    }
}
