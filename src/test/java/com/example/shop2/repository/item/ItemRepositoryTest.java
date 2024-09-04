package com.example.shop2.repository.item;

import static org.junit.jupiter.api.Assertions.*;

import com.example.shop2.constant.item.ItemSellState;
import com.example.shop2.dto.item.ItemSearchDto;
import com.example.shop2.dto.item.MainItemDto;
import com.example.shop2.entity.item.Item;
import com.example.shop2.entity.item.ItemImg;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemRepositoryTest {

    // 통합테스트
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemImgRepository itemImgRepository;

    @BeforeEach
    void setUp() {
        assertNotNull(itemRepository);
        assertNotNull(itemImgRepository);

        itemImgRepository.deleteAll();
        itemRepository.deleteAll();
    }

    /**
     * 기능 구현 목록
     * - 상품 등록
     * - 상품 삭제
     * - 상품 수정
     * - 상품 이름으로 조회
     * - 상품 가격으로 조회
     * - 상품 이름/상품 상세 내용으로 조회
     * - 상품 상태별 조회
     * - 상품 등록일로 조회
     * - 상품 수정일로 조회
     * - 탬색 기능 (getAdminItemImg)
     * - 메인 페이지 탐색 기능
     */

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    @DisplayName("1. 상품 등록")
    public void testSaveItem(int count) {
        // count 개수 만큼 아이템을 만듦
        for (int i=0; i<count; i++) {
            var item = createItem(i);
            // 저장된 아이템 조회
            Item savedItem = itemRepository.save(item);
            // 서로 동일한지 확인
            assertSameItem(item, savedItem);
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    @DisplayName("1. 상품 삭제")
    public void testDeleteItem(int count) {
        // count 개수 만큼 아이템을 만듦
        for (int i=0; i<count; i++) {
            var item = createItem(i);
            // 저장된 아이템 조회
            Item savedItem = itemRepository.save(item);
            // 서로 동일한지 확인
            assertSameItem(item, savedItem);
            // 해당 상품 삭제
            itemRepository.deleteById(savedItem.getId());
            // 해당 이름으로 상품 조회
            assertTrue(itemRepository.findById(savedItem.getId()).isEmpty());
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    @DisplayName("1. 상품 이름으로 조회")
    public void testFindByName(int count) {
        itemRepository.deleteAll();
        // count 개수 만큼 아이템을 만듦
        List<Item> dummy = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            dummy.add(createItem(i));
        }
        dummy.sort((o1, o2) -> o1.getItemNm().compareTo(o2.getItemNm()));

        // 아이템 저장
        for (Item item : dummy) {
            itemRepository.save(item);
        }

        // 개수가 count개 인지 확인
        long actualCnt = itemRepository.count();
        assertEquals(count, actualCnt);

        // 상품명으로 상품 조회
        // 내용이 동일한지 확인
        for (Item item : dummy) {
            List<Item> foundItems = itemRepository.findByItemNm(item.getItemNm());
            assertEquals(1, foundItems.size());
            assertSameItem(item, foundItems.get(0));
        }
    }

    @DisplayName("상품 이름으로 조회")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    public void testFindByItemNm(int count) {
        itemRepository.deleteAll();

        for (int i=0; i<count; i++) {
            Item item = createItem(i);
            Item savedItem = itemRepository.save(item);

            List<Item> foundItem = itemRepository.findByItemNm(savedItem.getItemNm());
            assertEquals(1, foundItem.size());
            assertSameItem(savedItem, foundItem.get(0));
        }
    }


    @DisplayName("상품 가격으로 조회")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    public void testFindByPrice(int count) {
        itemRepository.deleteAll();
        List<Item> dummy = createItemDummy(count);

        for (Item item : dummy) {
            itemRepository.save(item);
        }

        List<Item> foundItems = itemRepository.findByPriceLessThan(1000L);
        assertEquals(1, foundItems.size());

    }

    @DisplayName("상품 이름/상품 상세 내용으로 조회")
    @ParameterizedTest
    @ValueSource(ints = {2, 5, 10, 15, 20})
    public void testFindItemNmOrItemDetail(int count) {
        itemRepository.deleteAll();
        List<Item> dummy = createItemDummy(count);

        for (Item item : dummy) {
            itemRepository.save(item);
        }

        List<Item> foundItems = itemRepository.findByItemNmOrItemDetail("item1", "item detail1");
        assertEquals(1, foundItems.size());

    }

    @DisplayName("상품 탐색 기능")
    @Test
    public void testGetAdminItemPage() {
        // given
        // 0. 데이터를 저장
        // 1. 검색 조건 생성
        // 2. 페이징 정보 생성
        itemRepository.deleteAll();
        List<Item> dummy = createItemDummy(20);
        for (Item item : dummy) {
            itemRepository.save(item);
        }

        ItemSearchDto search = createItemSearchDto("itemNm", "item", "1w",ItemSellState.SELL); // 모든 상품 조회하는 조건
        Pageable pageable = createPageable(0, 10); // 0, 10

        // when
        // 0. 관리자 페이지 조회 시도
        Page<Item> itemPage = itemRepository.getAdminItemPage(search, pageable);


        // then
        // 0. 조회된 데이터의 전체 페이지 수
        // 1. 조회된 데이터의 사이즈 수
        // 2. 조회된 데이터와 저장된 데이터의 일치 여부
        assertEquals(2, itemPage.getTotalPages());
        assertEquals(10, itemPage.getSize());
        List<Item> content = itemPage.getContent();

        for (int i=0; i<content.size(); i++) {
            assertSameItem(content.get(i), dummy.get(i));
        }

    }

    @DisplayName("상품 상세 설명 키워드로 탐색")
    @Test
    public void testFindByItemDetailContaining() {
        itemRepository.deleteAll();
        List<Item> dummy = createItemDummy(20);

        for (Item item : dummy) {
            itemRepository.save(item);
        }

        List<Item> foundItems = itemRepository.findByItemDetail("item detail" + (20-1));
        assertEquals(1, foundItems.size());
    }


    @DisplayName("메인 페이지 탐색")
    @Test
    public void testFindMainItemPage() {
        // given
        // 0. 데이터 저장(상품, 상품 이미지)
        // 1. 검색 조건 생성
        // 2. 페이징 정보 생성
        itemRepository.deleteAll();
        itemImgRepository.deleteAll();

        List<Item> dummy = createItemDummy(20);
        List<ItemImg> dummyImg = createItemImgDummy(60);


        // 0 - 012
        // 1 - 345
        // 3 - 678
        // 4 - 91011
        // ...
        // 또한, 0, 3, 6, 9, ... 는 대표 이미지로 선정
        // 위와 같은 패턴으로 맞춤 (상품 1개당 사진 3개씩 등록)
        for (int i=0; i< dummy.size(); i++) {
            Item item = dummy.get(i);
            itemRepository.save(item);

            for (int j=i*3; j<(i*3)+3; j++) {
                ItemImg itemImg = dummyImg.get(j);
                itemImg.setItem(item);
                itemImgRepository.save(itemImg);
            }
        }


        List<ItemImg> all = itemImgRepository.findAll();
        System.out.println(all);
        assertEquals(60, all.size());

        ItemSearchDto search = createItemSearchDto("itemNm", "item", "all", ItemSellState.SELL); // 모든 상품 조회하는 조건
        Pageable pageable = createPageable(1, 10); // 0, 10


        // when
        // 0. 메인 페이지 조회 시도
        Page<MainItemDto> mainItemPage = itemRepository.getMainItemPage(search, pageable);

        // then
        // 0. 조회된 데이터의 전체 페이지 수
        // 1. 조회된 데이터의 사이즈 수
        // 2. 조회된 데이터와 저장된 데이터의 일치 여부
        assertEquals(2, mainItemPage.getTotalPages());
        assertEquals(10, mainItemPage.getSize());
        List<MainItemDto> content = mainItemPage.getContent();
        System.out.println(content.size());

        for (int i=0; i<content.size(); i++) {
            System.out.println(content.get(i));
        }
    }

    @DisplayName("상품 사진을 상품 번호로 조회하는 경우")
    @Test
    public void testFindItemImg() {
        // given
        // 0. 데이터 저장
        Item item = createItem(1);
        Item savedItem = itemRepository.save(item);
        List<ItemImg> itemImgDummy = createItemImgDummy(3);
        for (ItemImg itemImg : itemImgDummy) {
            itemImg.setItem(item);
            itemImgRepository.save(itemImg);
        }

        // when
        // 0. 저장된 상품 번호로 상품 사진 조회
        List<ItemImg> foundImgs = itemImgRepository.findByItemIdOrderByItemIdAsc(
                savedItem.getId());

        // then
        // 0. 사이즈가 3
        // 1. 내용 일치함
        assertEquals(3, foundImgs.size());
        for (int i=0; i<3; i++) {
            assertEquals(itemImgDummy.get(i).getImgName(), foundImgs.get(i).getImgName());
        }

    }

    @DisplayName("상품 사진을 상품 번호와 대표 유무 표시로 조회하는 경우")
    @Test
    public void testFindItemImgWithRep() {
        // given
        // 0. 데이터 저장
        Item item = createItem(1);
        Item savedItem = itemRepository.save(item);

        List<ItemImg> itemImgDummy = createItemImgDummy(3);
        for (ItemImg itemImg : itemImgDummy) {
            itemImg.setItem(item);
            itemImgRepository.save(itemImg);
        }

        // when
        // 0. 저장된 상품 번호와 대표 사진으로 상품 사진 조회
        ItemImg foundImg = itemImgRepository.findByItemIdAndRegImgYn(savedItem.getId(), "Y");

        // then
        // 0. 사이즈 1
        // 1. 내용 일치함
        assertNotNull(foundImg);
        assertEquals(itemImgDummy.get(0).getImgName(), foundImg.getImgName());
    }

    private ItemSearchDto createItemSearchDto(String searchBy, String searchQuery, String searchDateType, ItemSellState searchSellState) {
        ItemSearchDto dto = new ItemSearchDto();

        dto.setSearchBy(searchBy);
        dto.setSearchQuery(searchQuery);
        dto.setSearchDateType(searchDateType);
        dto.setSearchSellState(ItemSellState.SELL);

        return dto;
    }

    private Pageable createPageable(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return pageable;
    }


    private List<Item> createItemDummy(int count) {
        List<Item> dummy = new ArrayList<>();
        for (int i=0; i<count; i++) {
            Item item = createItem(i);
            dummy.add(item);
        }
        return dummy;
    }

    private List<ItemImg> createItemImgDummy(int count) {
        List<ItemImg> dummy = new ArrayList<>();

        for (int i=0; i<count; i++) {
            ItemImg itemImg = createItemImg(i);
            dummy.add(itemImg);
        }

        return dummy;
    }


    private Item createItem(int i) {
        var item  = new Item();
        item.setItemNm("item" + i);
        item.setPrice(1000 * i);
        item.setItemDetail("item detail" + i);
        item.setItemSellState(ItemSellState.SELL);
        item.setRegTime(LocalDateTime.now());
        return item;
    }

    private ItemImg createItemImg(int i) {
        var itemImg = new ItemImg();

        itemImg.setImgName("img" + i);
        itemImg.setOriImgName("oriImg" + i);
        itemImg.setImgUrl("imgUrl" + i);
        if (i % 3 == 0) {
            itemImg.setRegImgYn("Y");
        } else {
            itemImg.setRegImgYn("N");
        }

        return itemImg;
    }

    private boolean assertSameItem(Item item1, Item item2) {
        return item1.getItemNm().equals(item2.getItemNm()) &&
                item1.getItemDetail().equals(item2.getItemDetail()) &&
                item1.getItemSellState().equals(item2.getItemSellState());
    }


}