package com.ljs.shop.service;

import com.ljs.shop.entity.User;
import com.ljs.shop.exception.DuplicateEmailException;
import com.ljs.shop.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 사용자 정보를 입력받아 회원가입을 처리하는 메서드
    @Transactional
    public User signUp(User user) {
        Optional<User> existingMember = userRepository.findByEmail(user.getEmail());

        if (existingMember.isPresent()) {
            // 이미 가입된 이메일이면 예외를 발생
            throw new DuplicateEmailException("이미 사용 중인 이메일 주소입니다.");
        }

        // 비밀번호 암호화
        user.encodePassword(passwordEncoder.encode(user.getPassword()));

        // 가입된 이메일이 없으면 회원가입
        return userRepository.save(user);
    }

    // 사용자 이메일을 기반으로 사용자 인증 정보를 로드하는 메서드
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원입니다."));
    }
}