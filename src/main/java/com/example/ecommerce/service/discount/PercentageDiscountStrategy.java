package com.example.ecommerce.service.discount;

import org.springframework.stereotype.Component;

@Component
public class PercentageDiscountStrategy implements DiscountStrategy{
    private final int discountPercentage = 10;

    public PercentageDiscountStrategy() {
    }

    @Override
    public int applyDiscount(int price, int count) {
        return price * count * (100 - discountPercentage) / 100;
    }
}
