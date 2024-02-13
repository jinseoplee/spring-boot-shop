package com.ljs.shop.repository;

import com.ljs.shop.dto.ItemSearchDto;
import com.ljs.shop.dto.MainItemDto;
import com.ljs.shop.dto.QMainItemDto;
import com.ljs.shop.entity.Item;
import com.ljs.shop.entity.QItem;
import com.ljs.shop.entity.QItemImage;
import com.ljs.shop.entity.enums.ItemSellStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

public class CustomItemRepositoryImpl implements CustomItemRepository {
    private JPAQueryFactory queryFactory;

    public CustomItemRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 상품 검색 조건에 따라 페이징된 결과를 반환하는 메서드.
     *
     * @param itemSearchDto 검색 조건을 담은 ItemSearchDto 객체
     * @param pageable      페이징 정보를 담은 Pageable 객체
     * @return 페이징된 상품 리스트
     */
    @Override
    public Page<Item> findBySearchCondition(ItemSearchDto itemSearchDto, Pageable pageable) {
        List<Item> content = queryFactory
                .selectFrom(QItem.item)
                .where(
                        dateTypeAfter(itemSearchDto.getDateType()),
                        itemSellStatusEq(itemSearchDto.getItemSellStatus()),
                        nameLike(itemSearchDto.getName())
                )
                .orderBy(QItem.item.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(Wildcard.count)
                .from(QItem.item)
                .where(
                        dateTypeAfter(itemSearchDto.getDateType()),
                        itemSellStatusEq(itemSearchDto.getItemSellStatus()),
                        nameLike(itemSearchDto.getName())
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    /**
     * 메인 페이지에 표시될 상품 목록을 반환하는 메서드.
     *
     * @return 메인 페이지에 표시될 상품 목록
     */
    @Override
    public List<MainItemDto> findMainItems() {
        QItem item = QItem.item;
        QItemImage itemImage = QItemImage.itemImage;

        return queryFactory
                .select(new QMainItemDto(
                        item.id,
                        item.name,
                        item.detail,
                        itemImage.imageUrl,
                        item.price
                ))
                .from(itemImage)
                .join(itemImage.item, item)
                .where(itemImage.repImageYn.eq("Y"))
                .orderBy(item.id.desc())
                .fetch();
    }

    /**
     * 날짜 유형에 따른 검색 조건을 생성하는 메서드.
     *
     * @param dateType 날짜 유형
     * @return 생성된 검색 조건
     */
    private BooleanExpression dateTypeAfter(String dateType) {
        LocalDateTime localDateTime = LocalDateTime.now();

        if (StringUtils.equals("all", dateType) || dateType == null) {
            return null;
        } else if (StringUtils.equals("1d", dateType)) {
            localDateTime = localDateTime.minusDays(1);
        } else if (StringUtils.equals("1w", dateType)) {
            localDateTime = localDateTime.minusWeeks(1);
        } else if (StringUtils.equals("1m", dateType)) {
            localDateTime = localDateTime.minusMonths(1);
        } else if (StringUtils.equals("6m", dateType)) {
            localDateTime = localDateTime.minusMonths(6);
        }

        return QItem.item.createdDate.after(localDateTime);
    }

    /**
     * 상품 판매 상태에 따른 검색 조건을 생성하는 메서드.
     *
     * @param itemSellStatus 상품 판매 상태
     * @return 생성된 검색 조건
     */
    private BooleanExpression itemSellStatusEq(ItemSellStatus itemSellStatus) {
        return itemSellStatus == null ? null : QItem.item.itemSellStatus.eq(itemSellStatus);
    }

    /**
     * 상품명에 대한 검색 조건을 생성하는 메서드.
     *
     * @param name 상품명
     * @return 생성된 검색 조건
     */
    private BooleanExpression nameLike(String name) {
        return name == null ? null : QItem.item.name.like("%" + name + "%");
    }
}
