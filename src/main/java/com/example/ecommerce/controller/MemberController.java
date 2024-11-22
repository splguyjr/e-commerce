package com.example.ecommerce.controller;

import com.example.ecommerce.dto.MemberForm;
import com.example.ecommerce.entity.Address;
import com.example.ecommerce.entity.Member;
import com.example.ecommerce.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/signup")
    private String createMemberForm(@ModelAttribute("memberForm") MemberForm memberForm, Model model) {
        return "member/createMemberForm";
    }

    @PostMapping("/signup")
    private String createMember(@Valid @ModelAttribute("memberForm") MemberForm memberForm, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            return "member/createMemberForm";
        }

        Address address = new Address(memberForm.getCity(), memberForm.getStreet(), memberForm.getZipcode());

        try {
            Member member = Member.builder()
                    .name(memberForm.getName())
                    .email(memberForm.getEmail())
                    .password(memberForm.getPassword())
                    .address(address)
                    .build();

            memberService.join(member);
        } catch (IllegalStateException e) {  //회원 이메일이 중복시 발생하는 예외
            model.addAttribute("errorMessage", e.getMessage());
            return "member/createMemberForm";
        }
        return "redirect:/member/login";
    }

}
