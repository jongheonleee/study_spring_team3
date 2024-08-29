package com.example.shop2.exception.global;

import com.example.shop2.error.ErrorCode;
import lombok.Getter;

@Getter
public class EmptyRequiredValuesException extends RuntimeException {
    private final ErrorCode errorCode;

    public EmptyRequiredValuesException(Throwable e, ErrorCode errorCode) {
        super(e);
        this.errorCode = errorCode;
    }
}
