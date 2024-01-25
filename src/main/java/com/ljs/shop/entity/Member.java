package com.ljs.shop.entity;

import com.ljs.shop.entity.enums.Role;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@NoArgsConstructor
@Getter
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    private String name;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @Builder
    public Member(String email, String password, String name, String address) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.address = address;
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }
}
