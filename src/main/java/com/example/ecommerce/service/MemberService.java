package com.example.ecommerce.service;

import com.example.ecommerce.entity.Member;
import com.example.ecommerce.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Long join(Member member) {
        //중복된 이메일의 회원이 있는지 검사 후 예외처리
        Optional<Member> findMember = memberRepository.findByEmail(member.getEmail());
        if (findMember.isPresent()) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }
        //해당 이메일의 회원이 없는 경우 정상적으로 저장
        Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }

    public Member findMember(Long id) {
        Optional<Member> findMember = memberRepository.findById(id);
        if(findMember.isEmpty()) {
            throw new IllegalStateException("존재하지 않는 회원입니다.");
        }

        return findMember.get();
    }

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
}
