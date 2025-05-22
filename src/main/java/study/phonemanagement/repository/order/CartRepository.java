package study.phonemanagement.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import study.phonemanagement.entity.cart.Cart;
import study.phonemanagement.entity.phone.Phone;
import study.phonemanagement.entity.user.User;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findAllByUser(User user);
    List<Cart> findAllByPhone(Phone phone);
}
