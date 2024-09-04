package com.example.shop2.service.member;

import com.example.shop2.dto.member.MemberFormDto;
import com.example.shop2.entity.member.Member;
import com.example.shop2.exception.global.EmptyRequiredValuesException;
import com.example.shop2.exception.global.RetryFailedException;
import com.example.shop2.exception.member.DuplicatedEmailException;

public interface MemberService {

    // 회원 등록
    Member create(MemberFormDto memberFormDto)
            throws DuplicatedEmailException, EmptyRequiredValuesException, RetryFailedException;

    // 로그인
    boolean isValidUser(MemberFormDto memberFormDto);
}
