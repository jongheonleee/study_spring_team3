package com.example.shop2.exception.member;

import com.example.shop2.error.ErrorCode;
import lombok.Data;
import lombok.Getter;
import org.springframework.dao.DataAccessException;

@Getter
public class DuplicatedEmailException extends DataAccessException {

    private final ErrorCode errorCode;

    public DuplicatedEmailException(Throwable e, ErrorCode errorCode) {
        super(errorCode.getMessage(), e);
        this.errorCode = errorCode;
    }

}
