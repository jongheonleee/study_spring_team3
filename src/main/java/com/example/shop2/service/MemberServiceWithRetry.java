//package com.example.shop2.service;
//
//import static com.example.shop2.error.MemberErrorCode.DuplicatedEmail;
//import static com.example.shop2.error.MemberErrorCode.EmptyRequiredValue;
//
//import com.example.shop2.dto.MemberFormDto;
//import com.example.shop2.entity.Member;
//import com.example.shop2.error.GlobalErrorCode;
//import com.example.shop2.exception.global.EmptyRequiredValuesException;
//import com.example.shop2.exception.global.RetryFailedException;
//import com.example.shop2.exception.member.DuplicatedEmailException;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.TransactionSystemException;
//
//@Slf4j
//@Service
//public class MemberServiceWithRetry implements MemberService {
//
//    private final int MAX_RETRIES = 10;
//    private final int INIT_RETRIES = 1;
//    private final int RETRY_DELAY = 1_000; // 1초
//
//    private final MemberService memberService;
//
//    @Autowired
//    public MemberServiceWithRetry(MemberService memberService) {
//        this.memberService = memberService;
//    }
//
//    @Override
//    public Member create(MemberFormDto memberFormDto)
//            throws DuplicatedEmailException, EmptyRequiredValuesException, RetryFailedException {
//        return null;
//    }
//
//    @Override
//    public boolean isValidUser(MemberFormDto memberFormDto) {
//        return false;
//    }
//}
