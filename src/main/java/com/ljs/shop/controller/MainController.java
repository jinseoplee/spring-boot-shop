package com.ljs.shop.controller;

import com.ljs.shop.dto.MainItemDto;
import com.ljs.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class MainController {
    private final ItemService itemService;

    // 메인 페이지
    @GetMapping("/")
    public String main(Model model) {
        List<MainItemDto> items = itemService.findMainItems();
        model.addAttribute("items", items);
        return "main";
    }
}