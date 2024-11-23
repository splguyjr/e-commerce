package com.example.ecommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginForm {

    @NotEmpty(message = "이메일을 입력해주세요")
    @Email
    private String email;

    @NotEmpty(message = "비밀번호를 입력해주세요")
    private String password;
}
