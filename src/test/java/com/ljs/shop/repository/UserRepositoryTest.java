package com.ljs.shop.repository;

import com.ljs.shop.entity.User;
import jakarta.persistence.EntityNotFoundException;
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
    @DisplayName("사용자 정보 저장 및 조회 테스트")
    public void saveAndRetrieveUserTest() {
        // given
        User user = createUser();
        userRepository.save(user);

        // when
        User savedUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(EntityNotFoundException::new);

        // then
        assertEquals(user.getEmail(), savedUser.getEmail());
        assertEquals(user.getPassword(), savedUser.getPassword());
        assertEquals(user.getName(), savedUser.getName());
        assertEquals(user.getAddress(), savedUser.getAddress());
        assertEquals(user.getRole(), savedUser.getRole());
    }

    private User createUser() {
        return User.builder()
                .email("test-email@test.com")
                .password("1234")
                .name("leejinseop")
                .address("seoul")
                .build();
    }
}