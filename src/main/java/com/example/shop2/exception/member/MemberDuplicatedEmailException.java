package com.example.shop2.exception.member;

import com.example.shop2.error.MemberErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberDuplicatedEmailException extends RuntimeException{

    private final MemberErrorCode errorCode;
}
