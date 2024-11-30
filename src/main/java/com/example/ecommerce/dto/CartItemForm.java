package com.example.ecommerce.dto;

import com.example.ecommerce.entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CartItemForm {
    //필요한 것:아이템 이름, 아이템 가격, 아이템 수량, 아이템id, imageUrl

    private Long cartItemId;

    private String name;

    private int price;

    private int stockQuantity;

    private String imageUrl;

    private int count;

    public static CartItemForm from(CartItem cartItem) {
        return CartItemForm.builder()
                .cartItemId(cartItem.getId())
                .name(cartItem.getItem().getName())
                .price(cartItem.getItem().getPrice())
                .stockQuantity(cartItem.getItem().getStockQuantity())
                .imageUrl(cartItem.getItem().getImageUrl())
                .count(cartItem.getCount())
                .build();
    }

}
