package com.ljs.shop.exception;

// 회원가입 시 중복된 이메일이 발생했을 때 발생하는 예외 클래스
public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String message) {
        super(message);
    }
}