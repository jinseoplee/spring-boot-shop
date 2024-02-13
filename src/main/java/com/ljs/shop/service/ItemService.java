package com.ljs.shop.service;

import com.ljs.shop.dto.ItemFormDto;
import com.ljs.shop.dto.ItemSearchDto;
import com.ljs.shop.entity.Item;
import com.ljs.shop.entity.ItemImage;
import com.ljs.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemImageService itemImageService;

    /**
     * 상품 및 이미지를 저장한다.
     *
     * @param itemFormDto       상품 정보 DTO
     * @param itemImageFileList 상품 이미지 파일 리스트
     * @throws Exception 상품 또는 이미지 저장 중 예외 발생 시
     */
    @Transactional
    public void saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImageFileList) throws Exception {
        // 상품 생성 및 저장
        Item savedItem = itemRepository.save(itemFormDto.toEntity());

        // 상품 이미지 저장
        for (int i = 0; i < itemImageFileList.size(); i++) {
            ItemImage itemImage = new ItemImage(savedItem, i == 0 ? "Y" : "N");
            itemImageService.saveItemImage(itemImage, itemImageFileList.get(i));
        }
    }

    /**
     * 상품 검색 조건에 따라 페이지네이션된 상품 목록을 반환한다.
     *
     * @param itemSearchDto 검색 조건을 담은 ItemSearchDto 객체
     * @param pageable      페이지네이션 정보를 담은 Pageable 객체
     * @return 페이지네이션된 상품 목록
     */
    @Transactional(readOnly = true)
    public Page<Item> findBySearchCondition(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.findBySearchCondition(itemSearchDto, pageable);
    }
}
