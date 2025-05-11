package study.phonemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.phonemanagement.entity.cart.Cart;
import study.phonemanagement.entity.user.User;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findAllByUser(User user);
}
