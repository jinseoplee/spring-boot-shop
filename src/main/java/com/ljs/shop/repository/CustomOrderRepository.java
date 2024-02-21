package com.ljs.shop.repository;

import com.ljs.shop.entity.Order;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomOrderRepository {
    /**
     * 특정 이메일로 주문 목록을 조회하는 메서드
     *
     * @param email    회원 이메일
     * @param pageable 페이지 정보
     * @return 주문 목록
     */
    List<Order> findMyOrders(String email, Pageable pageable);

    /**
     * 특정 이메일로 주문 수를 조회하는 메서드
     *
     * @param email 회원 이메일
     * @return 주문 수
     */
    Long countOrders(String email);
}
