package study.phonemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.phonemanagement.entity.order.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
