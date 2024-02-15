package com.ljs.shop.service;

import com.ljs.shop.dto.OrderDto;
import com.ljs.shop.entity.Item;
import com.ljs.shop.entity.Member;
import com.ljs.shop.entity.Order;
import com.ljs.shop.entity.OrderItem;
import com.ljs.shop.repository.ItemRepository;
import com.ljs.shop.repository.MemberRepository;
import com.ljs.shop.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    /**
     * 주문을 처리하는 메서드
     *
     * @param orderDto 주문할 상품 정보를 담은 DTO 객체
     * @param email    주문을 요청한 회원의 이메일
     * @return 생성된 주문의 ID
     * @throws EntityNotFoundException 주문을 요청한 회원 또는 상품이 존재하지 않을 경우 발생
     */
    @Transactional
    public Long order(OrderDto orderDto, String email) {
        // 상품을 주문한 회원 조회
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);

        // 상품 조회
        Item item = itemRepository.findById(orderDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);

        // 주문 상품을 저장하는 리스트 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
        List<OrderItem> orderItems = Collections.singletonList(orderItem);

        // 주문 생성 및 저장
        Order order = Order.createOrder(member, orderItems);
        orderRepository.save(order);

        return order.getId();
    }
}
