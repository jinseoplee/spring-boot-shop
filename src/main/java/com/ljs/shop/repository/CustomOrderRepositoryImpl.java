package com.ljs.shop.repository;

import com.ljs.shop.entity.Order;
import com.ljs.shop.entity.QOrder;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class CustomOrderRepositoryImpl implements CustomOrderRepository {
    private JPAQueryFactory queryFactory;

    public CustomOrderRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 주문 목록을 반환한다.
     *
     * @param email    회원 이메일
     * @param pageable 페이지 정보
     * @return 주문 목록
     */
    @Override
    public List<Order> findMyOrders(String email, Pageable pageable) {
        QOrder order = QOrder.order;
        return queryFactory
                .selectFrom(order)
                .where(order.member.email.eq(email))
                .orderBy(order.createdDate.desc())
                .fetch();
    }

    /**
     * 주문 수를 반환한다.
     *
     * @param email 회원 이메일
     * @return 주문 수
     */
    @Override
    public Long countOrders(String email) {
        QOrder order = QOrder.order;
        return queryFactory
                .select(Wildcard.count)
                .from(order)
                .where(order.member.email.eq(email))
                .orderBy(order.createdDate.desc())
                .fetchOne();
    }
}
