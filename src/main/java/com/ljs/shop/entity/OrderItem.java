package com.ljs.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class OrderItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private int orderPrice; // 주문 가격
    private int count; // 수량

    private OrderItem(Item item, int count) {
        this.item = item;
        this.count = count;
        this.orderPrice = item.getPrice();
    }

    /**
     * 주문 상품을 생성하고 해당 상품의 재고를 감소시킨 후, 주문 항목을 반환한다.
     *
     * @param item  상품
     * @param count 상품 수량
     * @return 주문 상품
     */
    public static OrderItem createOrderItem(Item item, int count) {
        OrderItem orderItem = new OrderItem(item, count);
        item.decreaseStock(count);
        return orderItem;
    }

    /**
     * 주문 상품의 총 가격을 반환한다.
     *
     * @return 주문 상품의 총 가격
     */
    public int getTotalPrice() {
        return orderPrice * count;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    /**
     * 주문 취소 시 주문 수량만큼 상품의 재고를 더해주는 메서드
     */
    public void cancel() {
        this.getItem().addStock(count);
    }
}