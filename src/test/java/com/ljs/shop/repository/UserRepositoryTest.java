package com.ljs.shop.repository;

import com.ljs.shop.constant.Role;
import com.ljs.shop.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("사용자 정보 저장 테스트")
    public void saveUserTest() {
        // given
        String email = "test@test.com";
        String password = "1234";
        String name = "jinseop";
        String address = "seoul";
        Role role = Role.USER;

        User user = User.builder()
                .email(email)
                .password(password)
                .name(name)
                .address(address)
                .build();

        // when
        User savedUser = userRepository.save(user);

        // then
        assertEquals(email, savedUser.getEmail());
        assertEquals(password, savedUser.getPassword());
        assertEquals(name, savedUser.getName());
        assertEquals(address, savedUser.getAddress());
        assertEquals(role, savedUser.getRole());
    }
}