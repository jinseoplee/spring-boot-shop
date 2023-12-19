package com.ljs.shop.repository;

import com.ljs.shop.entity.Cart;
import com.ljs.shop.entity.User;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
class CartRepositoryTest {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("장바구니 회원 엔티티 매핑 조회 테스트")
    public void findCartAndUserTest() {
        // given
        User user = createUser("test-email@test.com", "1234", "leejinseop", "seoul");
        userRepository.save(user);

        Cart cart = new Cart(user);
        cartRepository.save(cart);

        flushAndClearEntityManager();

        // when
        Cart savedCart = cartRepository.findById(cart.getId())
                .orElseThrow(EntityExistsException::new);

        // then
        assertUser(user, savedCart.getUser());
    }

    private User createUser(String email, String password, String name, String address) {
        return User.builder()
                .email(email)
                .password(password)
                .name(name)
                .address(address)
                .build();
    }

    private void flushAndClearEntityManager() {
        entityManager.flush();
        entityManager.clear();
    }

    private void assertUser(User expectedUser, User actualUser) {
        assertEquals(expectedUser.getId(), actualUser.getId());
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        assertEquals(expectedUser.getPassword(), actualUser.getPassword());
        assertEquals(expectedUser.getName(), actualUser.getName());
        assertEquals(expectedUser.getAddress(), actualUser.getAddress());
    }
}