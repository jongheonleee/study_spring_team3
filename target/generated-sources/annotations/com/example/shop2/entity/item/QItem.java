package com.example.shop2.entity.item;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QItem is a Querydsl query type for Item
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItem extends EntityPathBase<Item> {

    private static final long serialVersionUID = 1860172710L;

    public static final QItem item = new QItem("item");

    public final StringPath createdBy = createString("createdBy");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath itemDetail = createString("itemDetail");

    public final StringPath itemNm = createString("itemNm");

    public final EnumPath<com.example.shop2.constant.item.ItemSellState> itemSellState = createEnum("itemSellState", com.example.shop2.constant.item.ItemSellState.class);

    public final StringPath modifiedAt = createString("modifiedAt");

    public final NumberPath<Long> price = createNumber("price", Long.class);

    public final DateTimePath<java.time.LocalDateTime> regTime = createDateTime("regTime", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> updateTime = createDateTime("updateTime", java.time.LocalDateTime.class);

    public QItem(String variable) {
        super(Item.class, forVariable(variable));
    }

    public QItem(Path<? extends Item> path) {
        super(path.getType(), path.getMetadata());
    }

    public QItem(PathMetadata metadata) {
        super(Item.class, metadata);
    }

}

