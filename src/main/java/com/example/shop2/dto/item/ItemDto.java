package com.example.shop2.dto.item;

import com.example.shop2.constant.item.ItemSellState;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter
@ToString
public class ItemDto {

    private Long id;
    private String itemNm;
    private Long price;
    private String itemDetail;
    private String itemSellState;
    private String createdBy;
    private String regTime;
    private String modifiedAt;
    private String updateTime;

}
