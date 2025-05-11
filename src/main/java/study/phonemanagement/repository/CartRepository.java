package study.phonemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.phonemanagement.entity.cart.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
