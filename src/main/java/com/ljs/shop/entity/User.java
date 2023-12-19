package com.ljs.shop.entity;

import com.ljs.shop.constant.Role;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "users")
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true)
    private String email; // 이메일

    @Column(nullable = false)
    private String password; // 비밀번호

    @Column(nullable = false)
    private String name; // 이름

    @Column(nullable = false)
    private String address; // 주소

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @Builder
    public User(String email, String password, String name, String address) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.address = address;
    }

    // 비밀번호를 암호화된 값으로 설정
    public void encodePassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}