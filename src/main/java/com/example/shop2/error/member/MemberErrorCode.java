package com.example.shop2.error.member;

import com.example.shop2.error.ErrorCode;
import org.springframework.http.HttpStatus;

public enum MemberErrorCode implements ErrorCode {

    NotFoundMember(HttpStatus.NOT_FOUND, "해당하는 회원이 없습니다."),
    DuplicatedEmail(HttpStatus.BAD_REQUEST, "중복된 이메일입니다."),
    EmptyRequiredValue(HttpStatus.BAD_REQUEST, "필수 값이 누락되었습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    private MemberErrorCode(HttpStatus httpStatus, String message) {
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