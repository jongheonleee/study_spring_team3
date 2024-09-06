package com.example.shop2.dto.item;

import com.example.shop2.constant.item.ItemSellState;
import com.example.shop2.entity.item.Item;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter @Setter
public class ItemFormDto {

    private Long id;

    @NotBlank(message = "상품명은 필수 입력값입니다.")
    private String itemNm;

    @NotNull(message = "가격은 필수 입력값입니다.")
    private Integer price;

    @NotBlank(message = "상품 상세설명은 필수 입력값입니다.")
    private String itemDetail;

    @NotNull(message = "재고수량은 필수 입력값입니다.")
    private Integer stockNumber;

    private ItemSellState itemSellStatus;

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    private List<Long> itemImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    // dto to entity
    public Item createItem() {
        return modelMapper.map(this, Item.class);
    }

    // entity to dto
    public static ItemFormDto of(Item item) {
        return modelMapper.map(item, ItemFormDto.class);
    }
}