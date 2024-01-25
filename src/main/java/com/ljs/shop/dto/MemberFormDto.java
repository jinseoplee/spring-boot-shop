package com.ljs.shop.dto;

import com.ljs.shop.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class MemberFormDto {
    @NotBlank(message = "이메일을 입력해 주세요.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @Length(min = 8, max = 16, message = "비밀번호는 8 ~ 16자로 사용하세요.")
    private String password;
    private String confirmPassword;

    @Length(min = 3, message = "이름은 최소 3자 이상이어야 합니다.")
    private String name;

    @NotBlank(message = "주소를 입력해 주세요.")
    private String address;

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .address(address)
                .build();
    }
}
