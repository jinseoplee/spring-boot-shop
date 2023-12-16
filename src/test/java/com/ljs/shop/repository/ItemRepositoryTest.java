package com.ljs.shop.repository;

import com.ljs.shop.constant.ItemSellStatus;
import com.ljs.shop.entity.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("아이템 저장 테스트")
    public void saveItemTest() {
        // given
        String name = "테스트 상품";
        int price = 10000;
        int stock = 20;
        String detail = "테스트 상품 상세 설명";
        ItemSellStatus itemSellStatus = ItemSellStatus.SELL;

        Item item = Item.builder()
                .name(name)
                .price(price)
                .stock(stock)
                .detail(detail)
                .itemSellStatus(itemSellStatus)
                .build();

        // when
        Item savedItem = itemRepository.save(item);

        entityManager.flush();
        entityManager.clear();

        // then
        assertEquals(name, savedItem.getName());
        assertEquals(price, savedItem.getPrice());
        assertEquals(stock, savedItem.getStock());
        assertEquals(detail, savedItem.getDetail());
        assertEquals(itemSellStatus, savedItem.getItemSellStatus());
    }
}