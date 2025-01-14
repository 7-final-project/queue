package com.qring.queue.application.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    AUTHORITY_ERROR(HttpStatus.FORBIDDEN, 2001), // NOTE : 권한 검증 에러
    DUPLICATE_ERROR(HttpStatus.BAD_REQUEST, 2002), // NOTE : 중복 검증 에러
    NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, 2003), // NOTE : 존재하지 않는 객체 에러
    BAD_REQUEST_ERROR(HttpStatus.BAD_REQUEST, 2004); // NOTE : 기타 에러

    private final HttpStatus httpStatus;
    private final Integer code;
}