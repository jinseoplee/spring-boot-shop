package com.ljs.shop.repository;

import com.ljs.shop.constant.ItemSellStatus;
import com.ljs.shop.entity.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;

    @Test
    @DisplayName("아이템 저장 및 조회 테스트")
    public void saveAndRetrieveItemTest() {
        // given
        Item item = createItem();
        itemRepository.save(item);

        // when
        List<Item> itemList = itemRepository.findAll();

        // then
        Item savedItem = itemList.get(0);
        assertEquals(item.getName(), savedItem.getName());
        assertEquals(item.getPrice(), savedItem.getPrice());
        assertEquals(item.getStock(), savedItem.getStock());
        assertEquals(item.getDetail(), savedItem.getDetail());
        assertEquals(item.getItemSellStatus(), savedItem.getItemSellStatus());
    }

    private Item createItem() {
        return Item.builder()
                .name("테스트 상품")
                .price(10000)
                .stock(10)
                .detail("테스트 상품 상세 설명")
                .itemSellStatus(ItemSellStatus.SELL)
                .build();
    }
}