package com.ljs.shop.repository;

import com.ljs.shop.dto.ItemSearchDto;
import com.ljs.shop.dto.MainItemDto;
import com.ljs.shop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomItemRepository {
    Page<Item> findBySearchCondition(ItemSearchDto itemSearchDto, Pageable pageable);

    List<MainItemDto> findMainItems();
}
