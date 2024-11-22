package com.example.ecommerce.dto;

import com.example.ecommerce.entity.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberForm {
    @NotEmpty(message = "이름은 필수입니다.")
    private String name;
    @NotEmpty(message = "이메일은 필수입니다.")
    @Email  //이메일 형식 검증
    private String email;
    @NotEmpty(message = "비밀번호는 필수입니다.")
    private String password;

    private String city;
    private String street;
    private String zipcode;
}
