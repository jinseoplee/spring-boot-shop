package com.ljs.shop.entity;

import com.ljs.shop.entity.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member; // 주문 회원

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.ORDER; // 주문 상태

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>(); // 주문 상품 리스트

    private Order(Member member) {
        this.member = member;
    }

    /**
     * 주문 객체를 생성하고 주문 아이템을 추가하여 반환한다.
     *
     * @param member     주문을 한 회원
     * @param orderItems 주문 상품 목록
     * @return 주문
     */
    public static Order createOrder(Member member, List<OrderItem> orderItems) {
        Order order = new Order(member);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        return order;
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    /**
     * 주문 상품의 총 가격을 반환한다.
     *
     * @return 주문 상품의 총 가격
     */
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    /**
     * 주문 상태를 CANCEL로 변경하고 주문 상품 수량을 상품 재고에 더한다.
     */
    public void cancelOrder() {
        this.orderStatus = OrderStatus.CANCEL;
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }
}
