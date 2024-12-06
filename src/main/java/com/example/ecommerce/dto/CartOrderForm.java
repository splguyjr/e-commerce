package com.example.ecommerce.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartOrderForm {

    @Builder
    public record OrderForm(Long itemId, int count, List<Long> wrappingMaterialList) {}

    private List<OrderForm> orderList;
}
