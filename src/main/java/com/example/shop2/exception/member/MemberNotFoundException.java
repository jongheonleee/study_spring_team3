package com.example.shop2.exception.member;

import com.example.shop2.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberNotFoundException extends RuntimeException {

    private final ErrorCode errorCode;

    public MemberNotFoundException(Throwable e, ErrorCode errorCode) {
        super(e);
        this.errorCode = errorCode;
    }
}