package com.example.shop2.repository.item;

import com.example.shop2.dto.item.ItemSearchDto;
import com.example.shop2.entity.item.Item;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

public interface ItemRepository
        extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {
    // 상품 이름으로 조회
    List<Item> findByItemNm(String itemNm);
    // 상품 가격으로 조회
    List<Item> findByPriceLessThan(Long price);
    // 상품 이름 또는 상품 상세 내용으로 조회
    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);
    // 상품 가격으로 조회하고 가격으로 정렬함
    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);
    // 상품 상세 내용으로 조회
    @Query("SELECT i FROM Item i WHERE i.itemDetail LIKE %:itemDetail% ORDER BY i.price DESC ")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

}
