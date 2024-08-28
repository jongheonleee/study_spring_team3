package com.example.shop2.controller;

import com.example.shop2.constant.Role;
import com.example.shop2.dto.MemberFormDto;
import com.example.shop2.entity.Member;
import com.example.shop2.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/members")
@RequiredArgsConstructor
@Controller
public class MemberController {

    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;

    /*
     * - 1차 기능 목록
     * (1) 회원 등록 페이지 이동
     * (2) 회원 등록 처리
     * (3) 회원 로그인 페이지 이동
     * (4) 회원 로그인 처리
     * (5) 회원 로그아웃 처리
     */



    // (1) 회원 등록 페이지 이동
        // 회원 등록 페이지 요청
        // 회원 등록 페이지 반환
    @GetMapping("/new")
    public String getMemberForm(MemberFormDto memberFormDto, String message, Model model) {
        model.addAttribute("memberFormDto", memberFormDto);
        model.addAttribute("errorMsg", message);
        return "member/memberForm";
    }

    // (2) 회원 등록 처리
        // 각 필드별로 유효성 검증, 실패하면 MSG & 회원 등록 폼 페이지로 다시 이동
        // 성공하면, 메인 페이지 이동
        // 실패하면, 실패 MSG & 회원 등록 폼 페이지로 다시 이동
        // 기존에 입력한 데이터 유지시킴
    @PostMapping("/new")
    public String addMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model, RedirectAttributes rdtt) {
        if (bindingResult.hasErrors()) {
            return "member/memberForm";
        }

        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder, Role.USER);
            memberService.create(member);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }

        return "redirect:/";
    }

    // (3) 회원 로그인 페이지 이동
    @GetMapping("/login")
    public String getLoginForm() {
        return "/member/loginForm";
    }

    @GetMapping("/login/error")
    public String getLoginErrorForm(Model model) {
        System.out.println("로그인 실패");
        model.addAttribute("loginErrorMsg", "로그인에 실패했습니다.");
        return "/member/loginForm";
    }



}
