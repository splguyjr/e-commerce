package com.example.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderForm {
    private Long itemId;
    private int count;
    private List<Long> wrappingMaterialList;
}
