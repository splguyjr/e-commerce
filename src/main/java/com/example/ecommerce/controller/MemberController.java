package com.example.ecommerce.controller;

import com.example.ecommerce.dto.LoginForm;
import com.example.ecommerce.dto.MemberForm;
import com.example.ecommerce.entity.Address;
import com.example.ecommerce.entity.Member;
import com.example.ecommerce.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@Slf4j
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/signup")
    private String createMemberForm(@ModelAttribute("memberForm") MemberForm memberForm, Model model) {
        return "member/createMemberForm";
    }

    @PostMapping("/signup")
    private String createMember(@Valid @ModelAttribute("memberForm") MemberForm memberForm, BindingResult bindingResult, Model model) {
        //입력 칸을 비워서 제출한 경우
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


    @GetMapping("/login")
    public String login(@ModelAttribute("loginForm") LoginForm loginForm, Model model) {
        return "member/loginForm";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginForm") LoginForm loginForm, BindingResult bindingResult, Model model, HttpServletRequest request) {
        String email = loginForm.getEmail();
        String password = loginForm.getPassword();

        //해당하는 이메일에 대한 비밀번호인지 확인, 아니면 null
        Member loginMember = memberService.login(email, password);

        if(loginMember == null) {
            //model.addAttribute("errorMessage", "유효하지 않은 이메일, 비밀번호의 조합입니다.");
            bindingResult.reject("loginError", "유효하지 않은 이메일, 비밀번호의 조합입니다.");
            return "member/loginForm";
        }
        log.info("user : {} login successful", loginMember.getEmail());

        //session 구현
        HttpSession session = request.getSession(true);//세션이 없으면 새로 생성
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if(session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }
}
