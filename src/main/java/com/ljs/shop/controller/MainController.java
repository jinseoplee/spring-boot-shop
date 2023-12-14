package com.ljs.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    // 메인 페이지를 보여주는 메서드
    @GetMapping("/")
    public String showMainPage() {
        return "main";
    }
}