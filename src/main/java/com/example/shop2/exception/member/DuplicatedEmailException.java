package com.example.shop2.exception.member;

import com.example.shop2.error.ErrorCode;
import lombok.Getter;

@Getter
public class DuplicatedEmailException extends RuntimeException{

    private final ErrorCode errorCode;

    public DuplicatedEmailException(Throwable e, ErrorCode errorCode) {
        super(e);
        this.errorCode = errorCode;
    }

}
