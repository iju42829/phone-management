package study.phonemanagement.repository.phone;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import study.phonemanagement.entity.phone.Manufacturer;
import study.phonemanagement.entity.phone.Phone;
import study.phonemanagement.entity.phone.Status;

import java.util.List;

import static study.phonemanagement.entity.phone.QPhone.*;

public class PhoneRepositoryImpl implements PhoneRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PhoneRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Phone> findAllPhone(Status status, String searchWord, Manufacturer manufacturer, Pageable pageable) {
        List<Phone> phones = queryFactory
                .selectFrom(phone)
                .where(phone.deletedAt.isNull(), searchWord(searchWord), manufacturer(manufacturer), status(status))
                .orderBy(phone.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(phone.count())
                .from(phone)
                .where(phone.deletedAt.isNull(), searchWord(searchWord), manufacturer(manufacturer), status(status))
                .fetchOne();

        return new PageImpl<>(phones, pageable, total);
    }

    private BooleanExpression searchWord(String searchWord) {
        return StringUtils.hasText(searchWord) ? phone.name.startsWith(searchWord) : null;
    }

    private BooleanExpression manufacturer(Manufacturer manufacturer) {
        return manufacturer == null ? null : phone.manufacturer.eq(manufacturer);
    }

    private BooleanExpression status(Status status) {
        return status == null ? null : phone.status.eq(status);
    }
}
