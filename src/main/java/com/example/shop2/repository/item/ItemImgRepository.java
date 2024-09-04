package com.example.shop2.repository.item;

import com.example.shop2.entity.item.ItemImg;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {
    List<ItemImg> findByItemIdOrderByItemIdAsc(Long itemId);
    ItemImg findByItemIdAndRegImgYn(Long itemId, String regImgYn);
}
