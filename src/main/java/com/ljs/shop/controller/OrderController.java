package com.ljs.shop.controller;

import com.ljs.shop.dto.OrderDto;
import com.ljs.shop.dto.OrderHistoryDto;
import com.ljs.shop.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class OrderController {
    private final OrderService orderService;

    @Value("${pagination.defaultPageSize}")
    private int defaultPageSize;

    @Value("${pagination.maxPage}")
    private int maxPage;

    @PostMapping("/order")
    public @ResponseBody
    ResponseEntity<?> order(@RequestBody @Valid OrderDto orderDto, BindingResult bindingResult, Principal principal) {
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

    @GetMapping({"/orders", "/orders/{page}"})
    public String orderHistory(@PathVariable("page") Optional<Integer> page, Principal principal, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, defaultPageSize);
        Page<OrderHistoryDto> orderHistoryDtoList = orderService.getOrderHistoryList(principal.getName(), pageable);
        model.addAttribute("orders", orderHistoryDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", maxPage);
        return "order/orderHistory";
    }
}
