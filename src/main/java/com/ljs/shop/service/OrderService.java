package com.ljs.shop.service;

import com.ljs.shop.dto.OrderDto;
import com.ljs.shop.dto.OrderHistoryDto;
import com.ljs.shop.dto.OrderItemDto;
import com.ljs.shop.entity.*;
import com.ljs.shop.repository.ItemImageRepository;
import com.ljs.shop.repository.ItemRepository;
import com.ljs.shop.repository.MemberRepository;
import com.ljs.shop.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final ItemImageRepository itemImageRepository;
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

    /**
     * 이메일에 해당하는 회원의 주문 목록을 페이지네이션하여 반환하는 메서드
     *
     * @param email    회원 이메일
     * @param pageable 페이지 정보
     * @return 주문 이력 페이지 객체
     */
    @Transactional(readOnly = true)
    public Page<OrderHistoryDto> getOrderHistoryList(String email, Pageable pageable) {
        // 사용자의 주문 목록을 조회한다.
        List<Order> orders = orderRepository.findMyOrders(email, pageable);

        // 사용자의 주문 수를 구한다.
        Long totalCount = orderRepository.countOrders(email);

        // 주문 이력들을 저장하는 리스트를 생성한다.
        List<OrderHistoryDto> orderHistoryDtoList = new ArrayList<>();

        // 주문 리스트를 순회하면서 주문 이력 페이지에 전달할 주문 이력 DTO를 생성한다.
        for (Order order : orders) {
            OrderHistoryDto orderHistoryDto = new OrderHistoryDto(order);

            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                // 주문 상품 대표 이미지를 조회한다.
                ItemImage itemImage = itemImageRepository.findByItemIdAndRepImageYn(orderItem.getItem().getId(), "Y");

                // 주문 상품 DTO를 생성하고 주문 이력 DTO에 추가한다.
                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImage.getImageUrl());
                orderHistoryDto.addOrderItemDto(orderItemDto);
            }

            // 주문 이력들을 저장하는 리스트에 주문 이력을 추가한다.
            orderHistoryDtoList.add(orderHistoryDto);
        }

        // 페이지 구현 객체를 생성하고 반환한다.
        return new PageImpl<>(orderHistoryDtoList, pageable, totalCount);
    }
}
