package com.example.shop2.controller;


import com.example.shop2.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MemberController {

    @Autowired
    private MemberService memberService;


}
