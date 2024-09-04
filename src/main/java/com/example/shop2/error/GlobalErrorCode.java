package com.example.shop2.error;

import org.springframework.http.HttpStatus;

public enum GlobalErrorCode implements ErrorCode {
    RetryFailed(HttpStatus.INTERNAL_SERVER_ERROR, "재시도 중 오류가 발생했습니다."),;

    private final HttpStatus httpStatus;
    private final String message;

    private GlobalErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
