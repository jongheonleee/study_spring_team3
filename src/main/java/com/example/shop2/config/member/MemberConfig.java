package com.example.shop2.config.member;


import com.example.shop2.repository.member.MemberRepository;
import com.example.shop2.service.member.MemberService;
import com.example.shop2.service.member.base.MemberServiceBase;
import com.example.shop2.service.member.expand.MemberServiceWithRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MemberConfig {


    private final MemberRepository memberRepository;

    @Autowired
    public MemberConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberService memberServiceBase() {
        return new MemberServiceBase(memberRepository);
    }

    @Bean
    public MemberService memberServiceWithRetry() {
        return new MemberServiceWithRetry(memberServiceBase());
    }
}
