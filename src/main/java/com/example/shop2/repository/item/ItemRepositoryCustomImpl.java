package com.example.shop2.repository.item;

import com.example.shop2.constant.item.ItemSellState;
import com.example.shop2.dto.item.ItemSearchDto;
import com.example.shop2.dto.item.MainItemDto;
import com.example.shop2.dto.item.QMainItemDto;
import com.example.shop2.entity.item.Item;
import com.example.shop2.entity.item.QItem;
import com.example.shop2.entity.item.QItemImg;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public ItemRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    // 상품 상태로 조회
    private BooleanExpression searchSellStatusEq(ItemSellState searchSellState) {
        return searchSellState == null ?
                null : QItem.item.itemSellState.eq(searchSellState);
    }

    // 기간 별로 조회할 때 사용하는 조건 부분
    private BooleanExpression regDtAfter(String searchDateType) {
        LocalDateTime dateTime = LocalDateTime.now();

        if (StringUtils.equals("all", searchDateType) || searchDateType == null) {
            return null;
        } else if (StringUtils.equals("1d", searchDateType)) {
            dateTime = dateTime.minusDays(1);
        } else if (StringUtils.equals("1w", searchDateType)) {
            dateTime = dateTime.minusWeeks(1);
        } else if (StringUtils.equals("1m", searchDateType)) {
            dateTime = dateTime.minusMonths(1);
        } else if (StringUtils.equals("6m", searchDateType)) {
            dateTime = dateTime.minusMonths(1);
        }

        return QItem.item.regTime.after(dateTime);
    }

    private BooleanExpression itemNmLike(String searchQuery) {
        return StringUtils.isEmpty(searchQuery) ? null : QItem.item
                .itemNm
                .like("%" +  searchQuery + "%");
    }

    // 제목, 이름 키워드를 기반으로 조회할 때 사용하는 조건 부분
    private BooleanExpression searchByLike(String searchBy, String searchQuery) {

        if (StringUtils.equals("itemNm", searchBy)) {
            return QItem.item.itemNm.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("createdBy", searchBy)) {
            return QItem.item.itemDetail.like("%" + searchQuery + "%");
        }

        return null;
    }

    // 상품 관리자 페이지에서 조회
    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        QueryResults<Item> results = queryFactory.selectFrom(QItem.item)
                                                 .where(regDtAfter(itemSearchDto.getSearchDateType()),
                                                         searchSellStatusEq(itemSearchDto.getSearchSellState()),
                                                         searchByLike(itemSearchDto.getSearchBy(),
                                                                      itemSearchDto.getSearchQuery()))
                                                                      .orderBy(QItem.item.id.desc())
                                                                      .offset(pageable.getOffset())
                                                                      .limit(pageable.getPageSize())
                                                .fetchResults();

        List<Item> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        QueryResults<MainItemDto> results = queryFactory.select(new QMainItemDto(
                                                                item.id,
                                                                item.itemNm,
                                                                item.itemDetail,
                                                                itemImg.imgUrl,
                                                                item.price))
                                                    .from(itemImg)
                                                    .join(itemImg.item, item)
                                                    .where(itemImg.regImgYn.eq("Y"))
                                                    .where(itemNmLike(itemSearchDto.getSearchQuery()))
                                                    .orderBy(item.id.desc())
                                                    .offset(pageable.getOffset())
                                                    .limit(pageable.getPageSize())
                                                    .fetchResults();

        List<MainItemDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }
}
