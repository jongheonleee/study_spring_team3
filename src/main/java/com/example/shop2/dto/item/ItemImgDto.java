package com.example.shop2.dto.item;

import com.example.shop2.entity.item.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter @Setter
public class ItemImgDto {


    private Long id;
    private String imgName;
    private String oriImgName;
    private String imgUrl;
    private String repImgYn;
    private static ModelMapper modelMapper = new ModelMapper();

    // entitiy to dto
    public static ItemImgDto of(ItemImg itemImg) {
        return modelMapper.map(itemImg, ItemImgDto.class);
    }
}
