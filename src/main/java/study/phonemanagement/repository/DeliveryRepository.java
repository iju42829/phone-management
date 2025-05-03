package study.phonemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.phonemanagement.entity.order.Delivery;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
