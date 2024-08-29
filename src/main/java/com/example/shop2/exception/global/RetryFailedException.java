package com.example.shop2.exception.global;

import com.example.shop2.error.ErrorCode;
import lombok.Getter;

@Getter
public class RetryFailedException extends RuntimeException {
    private final ErrorCode errorCode;

    public RetryFailedException(Throwable e, ErrorCode errorCode) {
        super(e);
        this.errorCode = errorCode;
    }

}
