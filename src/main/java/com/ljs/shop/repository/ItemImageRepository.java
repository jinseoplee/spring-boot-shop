package com.ljs.shop.repository;

import com.ljs.shop.entity.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {
    /**
     * 주어진 상품 ID에 해당하는 모든 상품 이미지를 조회한다.
     *
     * @param itemId 상품 ID
     * @return 상품 이미지 목록
     */
    List<ItemImage> findByItemId(Long itemId);
}
