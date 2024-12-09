package com.example.ecommerce.service;

import com.example.ecommerce.dto.MyPageForm;
import com.example.ecommerce.entity.Member;
import com.example.ecommerce.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private static MemberService singleton;

    private MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public static synchronized MemberService getInstance(MemberRepository memberRepository) {
        if (singleton == null) {
            singleton = new MemberService(memberRepository);
        }
        return singleton;
    }

    //회원가입
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

    //로그인
    public Member login(String email, String password) {
        //주어진 이메일에 대해 저장된 비밀번호와 제공된 비밀번호가 같은지 검증, 다르면 null 반환
        return memberRepository.findByEmail(email)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);
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
