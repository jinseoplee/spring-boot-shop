package com.ljs.shop.entity;

import com.ljs.shop.entity.enums.ItemSellStatus;
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

    private String detail; // 상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus = ItemSellStatus.SELL; // 상품 판매 상태

    @Builder
    public Item(String name, int price, int stock, String detail) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.detail = detail;
    }
}
