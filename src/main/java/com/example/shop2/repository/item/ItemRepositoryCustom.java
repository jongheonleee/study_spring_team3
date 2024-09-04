package com.example.shop2.repository.item;

import com.example.shop2.dto.item.ItemSearchDto;
import com.example.shop2.dto.item.MainItemDto;
import com.example.shop2.entity.item.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// Querydsl, Spring Data Jpa를 사용하기 위해선, 사용자 정의 리포지토리를 생성해야함
// - 1. 사용자 정의 인터페이스 작성
// - 2. 사용자 정의 인터페이스 구현
// - 3. Spring Data Jpa 리포지토리에서 사용자 정의 인터페이스 상속
public interface ItemRepositoryCustom {
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}
