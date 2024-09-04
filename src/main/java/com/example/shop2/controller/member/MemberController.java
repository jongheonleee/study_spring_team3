package com.example.shop2.controller.member;


import com.example.shop2.dto.member.MemberFormDto;
import com.example.shop2.exception.global.EmptyRequiredValuesException;
import com.example.shop2.exception.global.RetryFailedException;
import com.example.shop2.exception.member.DuplicatedEmailException;
import com.example.shop2.service.member.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/new")
    public String newMember(String errorMsg, Model model, MemberFormDto memberFormDto) {
        model.addAttribute("memberFormDto", memberFormDto);
        model.addAttribute("errorMsg", errorMsg);
        return "/member/registerForm";
    }

    @PostMapping("/new")
    public String createNewMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, RedirectAttributes rtts, Model model) {
        if (isInValidFields(bindingResult)) { // 💥 AOP 적용 대상
            rtts.addAttribute("errorMsg",  makeErrorMessage(bindingResult));
            return "redirect:/members/new";
        }

        try { // 💥 컨트롤러 어드바이스에서 다루기
            memberService.create(memberFormDto);
        } catch (DuplicatedEmailException | EmptyRequiredValuesException e) {
            rtts.addAttribute("errorMsg", e.getMessage());
            log.debug(e.getMessage());
            return "redirect:/members/new";
        } catch (RetryFailedException e) {
            model.addAttribute("errorMsg", e.getMessage());
            log.debug(e.getMessage());
            return "error";
        }

        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginForm(HttpServletRequest request, String errorMsg, Model model) {
        model.addAttribute("email", getEmailFromCookie(request));
        model.addAttribute("errorMsg", errorMsg);
        return "/member/loginForm";
    }

    @PostMapping("/login")
    public String login(MemberFormDto memberFormDto, @RequestParam(required = false) String rememberMe, HttpServletRequest request, HttpServletResponse response, RedirectAttributes rtts, Model model) {
        if (isAlreadyLogin(request, memberFormDto)
                || isValidMember(memberFormDto, request, response, rememberMe)) {
            return "redirect:/";
        }

        model.addAttribute("errorMsg", "이메일 또는 비밀번호가 올바르지 않습니다.");
        return "/member/loginForm";

    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null) {
            session.invalidate();
        }

        return "redirect:/";
    }

    private String getEmailFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("email")) {
                return cookie.getValue();
            }
        }

        return null;
    }

    private boolean isInValidFields(BindingResult bindingResult) {
        return bindingResult.hasErrors();
    }

    private String makeErrorMessage(BindingResult bindingResult) {
        var sb = new StringBuilder();
        for (FieldError e : bindingResult.getFieldErrors()) {
            sb.append("유효성 하지 않은 값을 입력하셨습니다.").append("\n")
                    .append(e.getDefaultMessage()).append("\n");
        }

        return sb.toString();
    }

    private boolean isAlreadyLogin(HttpServletRequest request, MemberFormDto memberFormDto) {
        HttpSession session = request.getSession();
        return session != null
                && session.getAttribute("email") != null
                && session.getAttribute("email").equals(memberFormDto.getEmail());
    }

    private boolean isValidMember(MemberFormDto memberFormDto, HttpServletRequest request, HttpServletResponse response, String rememberMe) {
        if (memberService.isValidUser(memberFormDto)) {
            HttpSession session = request.getSession();
            session.setAttribute("email", memberFormDto.getEmail());

            if ("on".equals(rememberMe)) {
                Cookie cookie = new Cookie("email", memberFormDto.getEmail());
                cookie.setMaxAge(24 * 60 * 60);
                response.addCookie(cookie);
            } else {
                Cookie cookie = new Cookie("email", "");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }

            return true;
        }

        return false;
    }


}
