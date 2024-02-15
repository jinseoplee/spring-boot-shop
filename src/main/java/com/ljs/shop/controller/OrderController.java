package com.ljs.shop.controller;

import com.ljs.shop.dto.OrderDto;
import com.ljs.shop.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/order")
    public @ResponseBody ResponseEntity<?> order(@RequestBody @Valid OrderDto orderDto, BindingResult bindingResult, Principal principal) {
        // 데이터 유효성 검사
        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        // 사용자 인증 확인
        if (principal == null) {
            // 인증되지 않은 사용자의 경우 UNAUTHORIZED 상태 코드 반환
            return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
        }

        // 주문한 사용자의 이메일 가져오기
        String email = principal.getName();
        Long orderId;

        try {
            // 주문 처리
            orderId = orderService.order(orderDto, email);
        } catch (Exception e) {
            // 주문 처리 중 예외 발생 시 BAD_REQUEST 상태 코드 반환
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        // 주문 처리 성공 시 주문 id와 OK 상태 코드 반환
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }
}
