package com.ljs.shop.dto;

import com.ljs.shop.entity.Item;
import com.ljs.shop.entity.enums.ItemSellStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ItemFormDto {
    private Long id;

    @NotBlank(message = "상품명을 입력해 주세요.")
    private String name; // 상품명

    @NotNull(message = "가격을 입력해 주세요.")
    private Integer price; // 가격

    @NotNull(message = "재고를 입력해 주세요.")
    private Integer stock; // 재고

    @NotBlank(message = "상품 상세 설명을 입력해 주세요.")
    private String detail; // 상품 상세 설명

    private ItemSellStatus itemSellStatus; // 상품 판매 상태

    private List<ItemImageDto> itemImageDtos = new ArrayList<>(); // 상품 이미지

    private List<Long> itemImageIds = new ArrayList<>();

    public Item toEntity() {
        return Item.builder()
                .name(name)
                .price(price)
                .stock(stock)
                .detail(detail)
                .build();
    }

    public ItemFormDto(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.price = item.getPrice();
        this.stock = item.getStock();
        this.detail = item.getDetail();
    }
}
