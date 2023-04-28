package com.zerobase.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	SAME_ITEM_NAME(HttpStatus.BAD_REQUEST, "아이템명 중복입니다."),
	NOT_FOUND_PRODUCT(HttpStatus.BAD_REQUEST, "상품이 존재하지 않습니다."),
	NOT_FOUND_PRODUCT_ITEM(HttpStatus.BAD_REQUEST, "상품 아이템이 존재하지 않습니다."),

	;
	private final HttpStatus httpStatus;
	private final String detail;
}
