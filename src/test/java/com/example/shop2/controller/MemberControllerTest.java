package com.example.shop2.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

import com.example.shop2.constant.Role;
import com.example.shop2.dto.MemberFormDto;
import com.example.shop2.entity.Member;
import com.example.shop2.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;



@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberControllerTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @BeforeEach
    public void setUp() {
        assertNotNull(mockMvc);
    }



    @Test
    @DisplayName("로그인 성공 테스트")
    public void loginSuccess() throws Exception {
        String email = "test@email.com";
        String password = "1234";
        var member = createMember(email, password);

        mockMvc.perform(formLogin().userParameter("email")
                        .loginProcessingUrl("/members/login")
                        .user(email).password(password))
                        .andExpect(SecurityMockMvcResultMatchers.authenticated());
    }

    private Member createMember(String email, String password) throws Exception {
        var memberFormDto = new MemberFormDto();
        memberFormDto.setEmail(email);
        memberFormDto.setPassword(password);
        memberFormDto.setAddress("서울시 강남구");
        memberFormDto.setName("홍길동");
        Member member = Member.createMember(memberFormDto, passwordEncoder, Role.USER);
        return memberService.create(member);

    }
}