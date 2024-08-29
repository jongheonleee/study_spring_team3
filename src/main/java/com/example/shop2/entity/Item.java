package com.example.shop2.entity;


import com.example.shop2.constant.ItemSellState;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "item")
@Getter @Setter
@ToString
public class Item {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String itemNm;
    private Long price;
    private String itemDetail;

    @Enumerated(EnumType.STRING)
    private ItemSellState itemSellState;
    private String createdBy;
    private LocalDateTime regTime;
    private String modifiedAt;
    private LocalDateTime updateTime;
}
