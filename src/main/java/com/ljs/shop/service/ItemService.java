package com.ljs.shop.service;

import com.ljs.shop.dto.ItemFormDto;
import com.ljs.shop.dto.ItemImageDto;
import com.ljs.shop.dto.ItemSearchDto;
import com.ljs.shop.dto.MainItemDto;
import com.ljs.shop.entity.Item;
import com.ljs.shop.entity.ItemImage;
import com.ljs.shop.repository.ItemImageRepository;
import com.ljs.shop.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemImageRepository itemImageRepository;
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

    /**
     * 주어진 상품 ID에 해당하는 상품의 세부 정보를 조회한다.
     *
     * @param itemId 조회할 상품의 ID
     * @return 상품의 세부 정보를 담은 DTO 객체
     * @throws EntityNotFoundException 주어진 상품 ID에 해당하는 상품이 존재하지 않을 경우 발생하는 예외
     */
    @Transactional(readOnly = true)
    public ItemFormDto getItemDetail(Long itemId) {
        // 상품 엔티티 조회
        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        // 상품 이미지 조회
        List<ItemImage> itemImages = itemImageRepository.findByItemId(itemId);

        // 조회된 상품 이미지를 DTO로 변환하여 리스트에 추가
        List<ItemImageDto> itemImageDtos = new ArrayList<>();
        for (ItemImage itemImage : itemImages) {
            itemImageDtos.add(new ItemImageDto(itemImage));
        }

        // 상품 정보와 상품 이미지 정보를 포함한 DTO 객체를 생성하여 반환
        ItemFormDto itemFormDto = new ItemFormDto(item);
        itemFormDto.setItemImageDtos(itemImageDtos);
        return itemFormDto;
    }

    /**
     * 메인 페이지에 표시될 상품 목록을 조회한다.
     *
     * @return 메인 페이지에 표시될 상품 목록
     */
    @Transactional(readOnly = true)
    public List<MainItemDto> findMainItems() {
        return itemRepository.findMainItems();
    }
}
