package com.ljs.shop.dto;

import com.ljs.shop.entity.ItemImage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemImageDto {
    private Long id;
    private String imageName; // 실제 로컬에 저장된 상품 이미지 이름
    private String originImageName; // 업로드했던 상품 이미지 이름
    private String imageUrl; // 이미지 조회 경로
    private String repImageYn; // 대표 이미지 여부

    public ItemImageDto(ItemImage itemImage) {
        this.id = itemImage.getId();
        this.imageName = itemImage.getImageName();
        this.originImageName = itemImage.getOriginImageName();
        this.imageUrl = itemImage.getImageUrl();
        this.repImageYn = itemImage.getRepImageYn();
    }
}
