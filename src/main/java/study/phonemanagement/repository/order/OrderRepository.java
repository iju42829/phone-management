package study.phonemanagement.repository.order;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import study.phonemanagement.entity.order.Order;
import study.phonemanagement.entity.user.User;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @EntityGraph(attributePaths = {"delivery"})
    Optional<Order> findWithDeliveryById(Long id);

    Page<Order> findAllByUser(User user, Pageable pageable);
}
