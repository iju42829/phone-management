package study.phonemanagement.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import study.phonemanagement.entity.order.Order;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = {"delivery"})
    Optional<Order> findWithDeliveryById(Long id);
}
