package com.zerobase.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    NOT_FOUND_PRODUCT(HttpStatus.BAD_REQUEST, "상품을 찾을 수 없습니다."),
    NOT_FOUND_PRODUCT_ITEM(HttpStatus.BAD_REQUEST, "상품 아이템을 찾을 수 없습니다."),
    SAME_ITEM_NAME(HttpStatus.BAD_REQUEST, "아이템 명 중복입니다."),

    CART_FAIL_CHANGE(HttpStatus.BAD_REQUEST, "장바구니에 추가할 수 없습니다."),
    ITEM_COUNT_NOT_ENOUGH(HttpStatus.BAD_REQUEST, "상품의 수량이 부족합니다."),

    ;

    private final HttpStatus httpStatus;
    private final String detail;
}
