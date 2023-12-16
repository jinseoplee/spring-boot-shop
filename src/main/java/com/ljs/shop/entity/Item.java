package com.ljs.shop.entity;

import com.ljs.shop.constant.ItemSellStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class Item extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item_id")
    private Long id; // 상품 코드

    @Column(nullable = false)
    private String name; // 상품 명

    private int price; // 가격

    private int stock; // 재고

    @Lob
    @Column(nullable = false)
    private String detail; // 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; // 판매 상태

    @Builder
    public Item(String name, int price, int stock, String detail, ItemSellStatus itemSellStatus) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.detail = detail;
        this.itemSellStatus = itemSellStatus;
    }
}