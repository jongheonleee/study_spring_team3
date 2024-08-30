package com.example.shop2.exception.member;

import com.example.shop2.error.ErrorCode;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;

@Getter
public class MemberNotFoundException extends DataAccessException {

    private final ErrorCode errorCode;

    public MemberNotFoundException(Throwable e, ErrorCode errorCode) {
        super(errorCode.getMessage(), e);
        this.errorCode = errorCode;
    }
}