package com.example.shop2.exception.global;

import com.example.shop2.error.ErrorCode;
import lombok.Getter;
import org.springframework.dao.DataAccessException;

@Getter
public class EmptyRequiredValuesException extends DataAccessException {
    private final ErrorCode errorCode;

    public EmptyRequiredValuesException(Throwable e, ErrorCode errorCode) {
        super(errorCode.getMessage(), e);
        this.errorCode = errorCode;
    }
}
