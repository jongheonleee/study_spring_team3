package com.example.shop2.dto.item;

import com.example.shop2.constant.item.ItemSellState;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemSearchDto {

    private String searchDateType;
    private ItemSellState searchSellState;
    private String searchBy;
    private String searchQuery = "";
}
