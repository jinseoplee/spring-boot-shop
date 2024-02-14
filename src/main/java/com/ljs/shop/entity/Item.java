package com.ljs.shop.entity;

import com.ljs.shop.entity.enums.ItemSellStatus;
import com.ljs.shop.exception.OutOfStockException;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class Item extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    private String name; // 상품명

    private int price; // 가격

    private int stock; // 재고

    @Column(length = 1000)
    private String detail; // 상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; // 상품 판매 상태

    @Builder
    public Item(String name, int price, int stock, String detail, ItemSellStatus itemSellStatus) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.detail = detail;
        this.itemSellStatus = itemSellStatus;
    }

    /**
     * 재고를 감소시키는 메서드
     *
     * @param count 상품 주문 수량
     * @throws OutOfStockException 상품 주문 수량이 재고보다 많을 때 발생
     */
    public void decreaseStock(int count) {
        int restStock = this.stock - count;
        if (restStock < 0) {
            throw new OutOfStockException("상품의 재고가 부족합니다.");
        }
        this.stock = restStock;
    }
}
