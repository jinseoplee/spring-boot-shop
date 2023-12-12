package com.ljs.shop.dto;

import com.ljs.shop.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class SignUpRequestDto {
    @NotBlank(message = "이메일을 입력하세요.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email; // 이메일

    @Length(min = 8, max = 16, message = "비밀번호는 8자 이상 16자 이하로 입력해주세요.")
    private String password; // 비밀번호

    @NotBlank(message = "이름을 입력하세요.")
    private String name; // 이름

    @NotBlank(message = "주소를 입력하세요.")
    private String address; // 주소

    public User toEntity() {
        return User.builder()
                .email(email)
                .password(password)
                .name(name)
                .address(address)
                .build();
    }
}