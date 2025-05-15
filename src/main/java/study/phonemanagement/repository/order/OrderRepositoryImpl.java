package study.phonemanagement.repository.order;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import study.phonemanagement.entity.order.Order;

import java.util.List;

import static study.phonemanagement.entity.order.QOrder.order;
import static study.phonemanagement.entity.user.QUser.user;

public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public OrderRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Order> findAllOrderByUsername(String username, Pageable pageable) {
        List<Order> orders = queryFactory.selectFrom(order)
                .where(searchUsername(username))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(order.count()).from(order).where(searchUsername(username)).fetchOne();

        return new PageImpl<>(orders, pageable, total);
    }

    private BooleanExpression searchUsername(String username) {
        return username == null ? null : user.username.eq(username);
    }
}
