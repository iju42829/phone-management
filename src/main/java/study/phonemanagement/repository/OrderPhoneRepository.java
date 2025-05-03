package study.phonemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.phonemanagement.entity.order.OrderPhone;

import java.util.Optional;

public interface OrderPhoneRepository extends JpaRepository<OrderPhone, Long> {
    Optional<OrderPhone> findByOrderId(Long orderId);
}
