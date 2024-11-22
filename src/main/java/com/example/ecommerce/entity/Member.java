package com.example.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.lang.Nullable;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String name;

    private String email;

    private String password;

    @Embedded
    @Nullable
    private Address address;

    @Enumerated(value = EnumType.STRING)
    @ColumnDefault("'BASIC'")
    private Grade grade;

    @Builder
    private Member(String name, String email, String password, Address address, Grade grade) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.grade = (grade == null) ? Grade.BASIC : grade;
    }
}
