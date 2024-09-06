package com.example.shop2.service.item;

import com.example.shop2.dto.item.ItemFormDto;
import com.example.shop2.dto.item.ItemImgDto;
import com.example.shop2.entity.item.Item;
import com.example.shop2.entity.item.ItemImg;
import com.example.shop2.repository.item.ItemImgRepository;
import com.example.shop2.repository.item.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;
    private final ItemImgService itemImgService;

    // 1. 상품 등록, 파라미터로 상품 입력 폼, 이미지 파일 리스트를 받음
    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {
        // 1. 상품 form 데이터 저장
        // 1-1. dto를 entity로 변환
        // 1-2. repository로 해당 상품 엔티티 저장
        Item item = itemFormDto.createItem();
        Item savedItem = itemRepository.save(item);

        // 2. 상품 이미지 저장
        // 2-1. 모든 상품 이미지를 순회함
        // 2-2. 이미지 엔티티를 생성함
        // 2-3. 상품과 연관관계 매핑해주기
        // 2-4. 각 상품 이미지를 등록함
        // 이때, 첫 번째 사진은 항상 대표 이미지로 등록함
        for (int i=0; i<itemImgFileList.size(); i++) {
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if (i == 0) {
                itemImg.setRegImgYn("Y");
            }
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }

        // 3. 등록된 상품 아이디 반환
        return savedItem.getId();
    }

    // 2. 기존의 상품 등록 페이지(상세 페이지)를 조회함
    @Transactional(readOnly = true)
    public ItemFormDto getItemDetail(Long id) {
        // 1. 상품의 id로 가져옴
        // 1-1. 상품 id로 해당 엔티티 조회함
        // 1-2. 해당 엔티티를 dto로 변환함
        Item foundItem = itemRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        ItemFormDto foundItemFormDto = ItemFormDto.of(foundItem);

        // 2. 상품의 id로 관련 이미지 가져옴
        // 2-1. 상품의 id로 해당 이미지 엔티티 목록을 조회함
        // 2-2. 해당 엔티티를 dto로 변환하고 List에 담음
        List<ItemImg> foundItemImgs = itemImgRepository.findByItemIdOrderByItemIdAsc(foundItem.getId());
        List<ItemImgDto> foundItemImgDtos = new ArrayList<>();
        for (ItemImg foundItemImg : foundItemImgs) {
            ItemImgDto foundItemImgDto = ItemImgDto.of(foundItemImg);
            foundItemImgDtos.add(foundItemImgDto);
        }

        // 3. ItemFormDto를 생성함
        // 3-1. 상품 dto를 넣어줌
        // 3-2. 상품 이미지 List<dto>를 넣어줌
        foundItemFormDto.setItemImgDtoList(foundItemImgDtos);

        // 4. ItemFormDto 반환
        return foundItemFormDto;
    }

}
