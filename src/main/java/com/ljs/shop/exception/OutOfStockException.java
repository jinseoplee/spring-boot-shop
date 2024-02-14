package com.ljs.shop.exception;

/**
 * 상품 주문 수량이 재고보다 많을 때 발생하는 예외
 */
public class OutOfStockException extends RuntimeException {
    public OutOfStockException(String message) {
        super(message);
    }
}
