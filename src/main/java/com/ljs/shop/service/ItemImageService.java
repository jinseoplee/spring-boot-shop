package com.ljs.shop.service;

import com.ljs.shop.entity.ItemImage;
import com.ljs.shop.repository.ItemImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Transactional
@Service
public class ItemImageService {
    private final ItemImageRepository itemImageRepository;
    private final FileService fileService;

    @Value("${itemImageLocation}")
    private String itemImageLocation;

    /**
     * 상품 이미지를 저장한다.
     *
     * @param itemImage     저장할 상품 이미지 엔티티
     * @param itemImageFile 상품 이미지 파일
     * @throws Exception 이미지 또는 상품 저장 중 예외 발생 시
     */
    public void saveItemImage(ItemImage itemImage, MultipartFile itemImageFile) throws Exception {
        // 업로드했던 상품 이미지 이름
        String originImageName = itemImageFile.getOriginalFilename();

        // 실제 로컬에 저장된 상품 이미지 이름 저장
        String imageName = fileService.uploadFile(itemImageLocation, originImageName, itemImageFile.getBytes());

        // 이미지 URL 생성
        String imageUrl = "/images/item/" + imageName;

        // 상품 이미지 업데이트 및 저장
        itemImage.updateItemImage(originImageName, imageName, imageUrl);
        itemImageRepository.save(itemImage);
    }
}
