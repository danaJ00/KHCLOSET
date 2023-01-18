package com.shop.dto;

import com.shop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {

    public OrderItemDto(OrderItem orderItem, String imgUrl) {
        this.itemNm = orderItem.getItem().getItemNm();
        this.itemDetail = orderItem.getItem().getItemDetail();
        this.itemPrice = orderItem.getItem().getPrice();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;
        this.itemId = orderItem.getItem().getId();
        this.totalPrice=orderItem.getCount()*orderItem.getOrderPrice();
    }
    private String itemNm;

    private Long itemId;

    private String itemDetail;

    private int totalPrice;

    private Integer itemPrice;

    private int count;

    private int orderPrice;

    private String imgUrl;
}
