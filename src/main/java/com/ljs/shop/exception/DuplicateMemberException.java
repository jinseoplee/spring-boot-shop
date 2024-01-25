package com.ljs.shop.exception;

public class DuplicateMemberException extends RuntimeException {
    public DuplicateMemberException() {
        super("이미 가입된 이메일입니다.");
    }
}
