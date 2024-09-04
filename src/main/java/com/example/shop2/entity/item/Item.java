package com.example.shop2.entity.item;


import com.example.shop2.constant.item.ItemSellState;
import com.example.shop2.dto.item.ItemDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "item")
@Getter @Setter
@NoArgsConstructor
@ToString
public class Item {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String itemNm;

    @NotNull
    private Long price;

    @NotNull
    private String itemDetail;

    @Enumerated(EnumType.STRING)
    private ItemSellState itemSellState;
    private String createdBy;
    private LocalDateTime regTime;
    private String modifiedAt;
    private LocalDateTime updateTime;

    public void updateItem(ItemDto itemDto) {
        this.setItemNm(itemDto.getItemNm());
        this.setPrice(itemDto.getPrice());
        this.setItemDetail(itemDto.getItemDetail());
        this.setItemSellState(ItemSellState.SELL);
    }
}
