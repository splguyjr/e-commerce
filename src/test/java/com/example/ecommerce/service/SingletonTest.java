package com.example.ecommerce.service;

import com.example.ecommerce.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SingletonTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void test() {
        MemberService instance1 = MemberService.getInstance(memberRepository);
        MemberService instance2 = MemberService.getInstance(memberRepository);

        //동일한 인스턴스 인지 검증
        Assertions.assertThat(instance1).isSameAs(instance2);
    }
}
