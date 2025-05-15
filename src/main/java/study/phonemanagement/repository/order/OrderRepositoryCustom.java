package study.phonemanagement.repository.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.phonemanagement.entity.order.Order;

public interface OrderRepositoryCustom {
    Page<Order> findAllOrderByUsername(String username, Pageable pageable);
}
