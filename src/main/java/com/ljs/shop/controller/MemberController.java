package com.ljs.shop.controller;

import com.ljs.shop.dto.MemberFormDto;
import com.ljs.shop.exception.DuplicateMemberException;
import com.ljs.shop.exception.PasswordMismatchException;
import com.ljs.shop.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/members")
@Controller
public class MemberController {
    private final MemberService memberService;

    // 회원 가입 페이지
    @GetMapping("/new")
    public String memberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    // 회원 가입
    @PostMapping("/new")
    public String createMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {
        // 유효성 검사 실패 시 회원 가입 폼 페이지로 이동
        if (bindingResult.hasErrors()) {
            return "member/memberForm";
        }

        try {
            // 회원 가입을 성공하면 메인 페이지로 리다이렉트
            memberService.createMember(memberFormDto);
            return "redirect:/";
        } catch (DuplicateMemberException e) {
            // 중복된 회원 예외 발생 시 에러 메시지와 함께 회원 가입 폼 페이지로 이동
            model.addAttribute("duplicateMemberError", e.getMessage());
            return "member/memberForm";
        } catch (PasswordMismatchException e) {
            // 비밀번호 불일치 예외 발생 시 에러 메시지와 함께 회원 가입 폼 페이지로 이동
            model.addAttribute("passwordMismatchError", e.getMessage());
            return "member/memberForm";
        }
    }
}