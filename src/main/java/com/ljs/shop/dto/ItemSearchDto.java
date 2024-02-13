package com.ljs.shop.dto;

import com.ljs.shop.entity.enums.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemSearchDto {
    // 검색 기간 타입(all, 1d, 1w, 1m, 6m)
    private String dateType;

    // 상품 판매 상태(SELL, SOLD_OUT)
    private ItemSellStatus itemSellStatus;

    // 상품명
    private String name;
}
