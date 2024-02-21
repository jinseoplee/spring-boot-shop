package com.ljs.shop.dto;

import com.ljs.shop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

/**
 * 주문 상품을 저장하는 DTO 클래스
 */
@Getter
@Setter
public class OrderItemDto {
    private String name; // 상품 명
    private int count; // 주문 수량
    private int orderPrice; // 주문 금액
    private String imageUrl; // 상품 이미지 경로

    public OrderItemDto(OrderItem orderItem, String imageUrl) {
        this.name = orderItem.getItem().getName();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imageUrl = imageUrl;
    }
}
