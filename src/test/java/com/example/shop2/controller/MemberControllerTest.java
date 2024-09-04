package com.example.shop2.controller;

import static com.example.shop2.error.member.MemberErrorCode.*;
import static com.example.shop2.error.member.MemberErrorCode.EmptyRequiredValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.example.shop2.constant.member.Role;
import com.example.shop2.controller.member.MemberController;
import com.example.shop2.dto.member.MemberFormDto;
import com.example.shop2.entity.member.Member;
import com.example.shop2.exception.global.EmptyRequiredValuesException;
import com.example.shop2.exception.member.DuplicatedEmailException;
import com.example.shop2.service.member.base.MemberServiceBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberServiceBase memberServiceBase;

    @MockBean
    private BindingResult bindingResult; // BindingResult를 모킹

    private MockHttpServletRequest mockRequest;
    private MockHttpSession mockSession;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockRequest = new MockHttpServletRequest();
        mockSession = new MockHttpSession(); // MockHttpSession 초기화
        mockRequest.setSession(mockSession); // 요청에 세션 설정
    }

    /**
     * '회원파트' 기능 목록
     * - 1. 회원 등록
     * - 2. 로그인
     * - 3. 로그아웃
     *
     * - Controller 기능 구현 내용
     * - 1. 회원 등록 처리 흐름
     * - 1-1. 회원 등록 성공 -> 메인 페이지 이동
     * - 1-2. 회원 등록 실패 : 회원 필드 유효하지 않음 -> 회원 등록 페이지로 이동 및 실패요인 메시지 전달
     * - 1-3. 회원 등록 실패 : 중보된 이메일 -> 회원 등록 페이지로 이동 및 실패요인 메시지 전달
     *
     * - 2. 회원 로그인 처리 흐름
     * - 2-1. 로그인 성공 -> 메인 페이지 이동
     * - 2-2. 로그인 실패 -> 로그인 페이지로 이동 및 실패요인 메시지 전달
     *
     * - 3. 회원 로그아웃 처리 흐름
     * - 3-1. 로그아웃 성공 -> 메인 페이지 이동
     *
     */


    @DisplayName("1-1. 회원 등록 성공 -> 메인 페이지 이동")
    @Test
    public void testMemberRegisterSuccess() throws Exception{
        when(memberServiceBase
                .create(any(MemberFormDto.class)))
                .thenReturn(new Member());

        when(bindingResult.hasErrors())
                .thenReturn(false);

        var memberFormDto = createMemberFormDto(1);


        mockMvc.perform(post("/members/new")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("memberFormDto", memberFormDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

    }

    @DisplayName("1-2. 회원 등록 실패 : 회원 필드 누락 -> 회원 등록 페이지로 이동 및 실패요인 메시지 전달")
    @Test
    public void testMemberFieldInvalidatedFailed() throws Exception {
        when(memberServiceBase
                .create(any(MemberFormDto.class)))
                .thenThrow(new EmptyRequiredValuesException(null, EmptyRequiredValue));

        when(bindingResult.hasErrors())
                .thenReturn(false);

        var memberFormDto = createMemberFormDto(1);

        mockMvc.perform(post("/members/new")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("memberFormDto", memberFormDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/members/new"));
    }

    @DisplayName("1-3. 회원 등록 실패 : 중복된 이메일 -> 회원 등록 페이지로 이동 및 실패요인 메시지 전달")
    @Test
    public void testMemberDuplicatedEmailFailed() throws Exception {
        when(memberServiceBase
                .create(any(MemberFormDto.class)))
                .thenThrow(new DuplicatedEmailException(null, DuplicatedEmail));

        when(bindingResult.hasErrors())
                .thenReturn(false);

        var memberFormDto = createMemberFormDto(1);

        mockMvc.perform(post("/members/new")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("memberFormDto", memberFormDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/members/new"));
    }

    @DisplayName("2-1. 로그인 성공 -> 메인 페이지 이동")
    @Test
    public void testMemberLoginSuccess() throws Exception{
        when(memberServiceBase
                .isValidUser(any(MemberFormDto.class)))
                .thenReturn(true);

        mockMvc.perform(post("/members/login"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @DisplayName("2-2. 로그인 실패 -> 로그인 페이지로 이동 및 실패요인 메시지 전달")
    @Test
    public void testMemberLoginFailed() throws Exception{
        when(memberServiceBase
                .isValidUser(any(MemberFormDto.class)))
                .thenReturn(false);

        mockMvc.perform(post("/members/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("/member/loginForm"));
    }

    @DisplayName("3-1. 로그아웃 성공 -> 메인 페이지 이동")
    @Test
    public void testMemberLogoutSuccess() throws Exception {
        mockSession.setAttribute("email", "testemail");

        // MockHttpServletRequest에 세션 설정
        mockRequest.setSession(mockSession);

        // 로그아웃 요청을 수행하고 세션을 무효화하며, 리다이렉트 확인
        mockMvc.perform(get("/members/logout")
                        .session(mockSession)) // MockHttpSession 포함
                .andExpect(status().is3xxRedirection()) // 3xx 리다이렉션 상태 코드 확인
                .andExpect(redirectedUrl("/")); // 리다이렉트된 URL이 "/"인지 확인

        // 정확히 세션이 무효화되었는지 확인
        assertTrue(mockSession.isInvalid()); // 여기서는 MockHttpSession을 수동으로 null 체크

    }

    private MemberFormDto createMemberFormDto(int i) {
        var memberFormDto = new MemberFormDto();

        memberFormDto.setEmail("test@gmail.com" + i);
        memberFormDto.setAddress("서울시 강남구");
        memberFormDto.setName("홍길동" + i);
        memberFormDto.setPassword("testpassword" + i);
        memberFormDto.setRole(Role.USER.name());

        return memberFormDto;
    }



}