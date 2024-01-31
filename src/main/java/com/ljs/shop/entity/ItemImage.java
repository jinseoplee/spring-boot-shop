package com.ljs.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class ItemImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_image_id")
    private Long id;

    private String imageName; // 실제 로컬에 저장된 상품 이미지 이름
    private String originImageName; // 업로드했던 상품 이미지 이름
    private String imageUrl; // 이미지 조회 경로
    private String repImageYn; // 대표 이미지 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    public void updateItemImage(String imageName, String originImageName, String imageUrl) {
        this.imageName = imageName;
        this.originImageName = originImageName;
        this.imageUrl = imageUrl;
    }

    public ItemImage(Item item, String repImageYn) {
        this.item = item;
        this.repImageYn = repImageYn;
    }
}
