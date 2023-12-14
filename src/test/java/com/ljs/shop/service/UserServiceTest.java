package com.ljs.shop.service;

import com.ljs.shop.entity.User;
import com.ljs.shop.exception.DuplicateEmailException;
import com.ljs.shop.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "1234";
    private static final String ENCODED_PASSWORD = "encodedPassword";
    private static final String NAME = "jinseop";
    private static final String ADDRESS = "seoul";

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("회원 가입 성공 테스트")
    public void signUpSuccessTest() {
        // given
        User user = User.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .name(NAME)
                .address(ADDRESS)
                .build();

        when(userRepository.save(user)).thenReturn(user);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);

        // when
        User savedUser = userService.signUp(user);

        // then
        assertEquals(EMAIL, savedUser.getEmail());
        assertEquals(ENCODED_PASSWORD, savedUser.getPassword());
        assertEquals(NAME, savedUser.getName());
        assertEquals(ADDRESS, savedUser.getAddress());
    }

    @Test
    @DisplayName("이메일 중복 시 예외 발생 테스트")
    public void signUpShouldThrowExceptionWhenEmailIsDuplicate() {
        // given
        User existingUser = User.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .name(NAME)
                .address(ADDRESS)
                .build();

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(existingUser));

        User newUser = User.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .name(NAME)
                .address(ADDRESS)
                .build();

        // when, then
        assertThrows(DuplicateEmailException.class, () -> userService.signUp(newUser));
    }
}