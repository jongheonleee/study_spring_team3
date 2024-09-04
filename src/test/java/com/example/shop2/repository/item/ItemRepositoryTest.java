package com.example.shop2.repository.item;

import static org.junit.jupiter.api.Assertions.*;

import com.example.shop2.constant.item.ItemSellState;
import com.example.shop2.entity.item.Item;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        assertNotNull(itemRepository);
    }

    /**
     * 기능 구현 목록
     * - 상품 등록
     * - 상품 삭제
     * - 상품 수정
     * - 1. 상품 이름으로 조회
     * - 2. 상품 가격으로 조회
     * - 3. 상품 이름/상품 상세 내용으로 조회
     * - 6. 상품 상태별 조회
     * - 7. 상품 등록일로 조회
     * - 8. 상품 수정일로 조회
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



    private List<Item> createItemDummy(int count) {
        List<Item> dummy = new ArrayList<>();
        for (int i=0; i<count; i++) {
            Item item = createItem(i);
            dummy.add(item);
        }
        return dummy;
    }


    private Item createItem(int i) {
        var item  = new Item();
        item.setItemNm("item" + i);
        item.setPrice(1000L * i);
        item.setItemDetail("item detail" + i);
        item.setItemSellState(ItemSellState.SELL);
        return item;
    }

    private boolean assertSameItem(Item item1, Item item2) {
        return item1.getItemNm().equals(item2.getItemNm()) &&
                item1.getPrice().equals(item2.getPrice()) &&
                item1.getItemDetail().equals(item2.getItemDetail()) &&
                item1.getItemSellState().equals(item2.getItemSellState());
    }


}