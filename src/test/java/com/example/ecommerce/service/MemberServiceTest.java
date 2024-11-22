package com.example.ecommerce.service;

import com.example.ecommerce.entity.Address;
import com.example.ecommerce.entity.Grade;
import com.example.ecommerce.entity.Member;
import com.example.ecommerce.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Optional;

@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;

    private Member createMember() {
        return Member.builder()
                .name("woonkyung")
                .password("1234")
                .address(new Address("Gyeonggi", "Yeongtong", "12345"))
                .email("abcde@naver.com")
                .build();
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void join() {
        //given
        Member member = this.createMember();

        //when
        Long savedId = memberService.join(member);

        //then
        Assertions.assertEquals(member, memberService.findMember(savedId));
    }

    @Test
    @DisplayName("중복회원 검증 테스트")
    public void checkDuplicate() {
        //given
        Member member = this.createMember();

        //when
        memberService.join(member);//member 저장
        try {
            memberService.join(member);//앞서 저장한 member 저장 시도
        } catch (IllegalStateException e) {
            return;
        }

        //then
        Assertions.fail("예외 발생");
    }


//    @Test
//    @DisplayName("멤버 초기 생성시 BASIC 등급인지 확인")
//    public void setRole() {
//        //given
//        Member member = createMember();
//
//        //when
//        Long savedId = memberService.join(member);
//
//        //then
//        Assertions.assertEquals(Grade.BASIC, memberService.findMember(savedId).getGrade());
//    }
}
