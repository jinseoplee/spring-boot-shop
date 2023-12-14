package com.ljs.shop.controller;

import com.ljs.shop.dto.SignUpRequestDto;
import com.ljs.shop.exception.DuplicateEmailException;
import com.ljs.shop.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    // 회원 가입 페이지를 보여주는 메서드
    @GetMapping("/signup")
    public String showSignUpForm(Model model) {
        model.addAttribute("signUpRequestDto", new SignUpRequestDto());
        return "user/signUpForm";
    }

    // 회원 가입 처리를 수행하는 메서드
    @PostMapping("/signup")
    public String processSingUp(@Valid SignUpRequestDto signUpRequestDto, BindingResult bindingResult, Model model) {
        // 유효성 검사 결과에 오류가 있으면 회원 가입 폼으로 돌아간다.
        if (bindingResult.hasErrors()) {
            return "user/signUpForm";
        }

        try {
            // 회원 가입 로직 수행
            userService.signUp(signUpRequestDto.toEntity());
        } catch (DuplicateEmailException e) {
            // 이메일 중복이 발생한 경우
            model.addAttribute("errorMessage", e.getMessage());
            return "user/signUpForm";
        }

        // 회원 가입이 성공하면 홈페이지로 리다이렉트
        return "redirect:/";
    }

    // 로그인 페이지를 보여주는 메서드
    @GetMapping("/signin")
    public String showSignInForm() {
        return "user/signInForm";
    }

    // 로그인 실패 시 모델에 에러 메시지를 추가하고 로그인 페이지를 보여주는 메서드
    @GetMapping("/signin/error")
    public String showSignInError(Model model) {
        model.addAttribute("signInErrorMessage", "아이디 또는 비밀번호가 일치하지 않습니다.");
        return "user/signInForm";
    }
}