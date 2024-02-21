package com.ljs.shop.dto;

import com.ljs.shop.entity.Order;
import com.ljs.shop.entity.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 주문 이력을 저장하는 DTO 클래스
 */
@Getter
@Setter
public class OrderHistoryDto {
    private Long orderId;
    private String orderDate; // 주문 날짜
    private OrderStatus orderStatus; // 주문 상태
    private List<OrderItemDto> orderItemDtoList = new ArrayList<>(); // 주문 상품 목록

    public OrderHistoryDto(Order order) {
        this.orderId = order.getId();
        this.orderDate = order.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();
    }

    /**
     * 주문 상품을 주문 상품 목록에 추가하는 메서드
     *
     * @param orderItemDto 주문 상품을 저장하는 DTO
     */
    public void addOrderItemDto(OrderItemDto orderItemDto) {
        orderItemDtoList.add(orderItemDto);
    }
}