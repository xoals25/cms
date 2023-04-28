package com.zerobase.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	No(HttpStatus.BAD_REQUEST, "no")
	;
	private final HttpStatus httpStatus;
	private final String detail;
}
