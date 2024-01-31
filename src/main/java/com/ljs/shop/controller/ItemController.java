package com.ljs.shop.controller;

import com.ljs.shop.dto.ItemFormDto;
import com.ljs.shop.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("/admin/item")
@Controller
public class ItemController {
    private static final String ERROR_MESSAGE = "최소 1개 이상의 사진을 등록해주세요.";
    private static final String FAILURE_MESSAGE = "상품 등록에 실패하였습니다. 잠시 후 다시 시도해주세요.";
    private final ItemService itemService;

    // 상품 등록 페이지
    @GetMapping("/new")
    public String itemForm(Model model) {
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "/item/itemForm";
    }

    // 상품 등록
    @PostMapping("/new")
    public String createItem(@Valid ItemFormDto itemFormDto,
                             BindingResult bindingResult,
                             @RequestParam("itemImageFile") List<MultipartFile> itemImageFileList,
                             Model model) {
        // 유효성 검사 실패 시 상품 등록 페이지로 이동
        if (bindingResult.hasErrors()) {
            return "/item/itemForm";
        }

        // 사진을 등록하지 않을 경우 상품 등록 페이지로 이동
        if (itemImageFileList.get(0).isEmpty()) {
            model.addAttribute("errorMessage", ERROR_MESSAGE);
            return "/item/itemForm";
        }

        try {
            List<MultipartFile> uploadFiles = filterNonEmptyFiles(itemImageFileList);

            // 상품 등록
            itemService.saveItem(itemFormDto, uploadFiles);
        } catch (Exception e) {
            model.addAttribute("errorMessage", FAILURE_MESSAGE);
            return "/item/itemForm";
        }

        // 상품이 정상적으로 등록되었다면 메인 페이지로 이동
        return "redirect:/";
    }


    /**
     * 사용자가 업로드한 이미지 파일을 반환
     *
     * @param itemImageFileList 필터링할 이미지 파일 리스트
     * @return 업로드한 파일 리스트
     */
    private List<MultipartFile> filterNonEmptyFiles(List<MultipartFile> itemImageFileList) {
        return itemImageFileList.stream()
                .filter(image -> !image.isEmpty())
                .collect(Collectors.toList());
    }
}
