package com.example.shop2.dto.item;

import com.example.shop2.constant.item.ItemSellState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemFormDto {

    private Long id;

    @NotBlank(message = "상품명은 필수값입니다.")
    private String itemNm;

    @NotNull(message = "가격은 필수값입니다.")
    private Integer price;

    @NotBlank(message = "상품 상세설명은 필수값입니다.")
    private String itemDetail;

    @NotNull(message = "재고수량은 필수값입니다.")
    private Integer stockNumber;

    private ItemSellState itemSellState;

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    private List<Long> itemImgIds = new ArrayList<>();

//    private static ModelMapper modelMapper = new ModelMapper();
}
