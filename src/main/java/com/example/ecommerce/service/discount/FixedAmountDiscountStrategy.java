package com.example.ecommerce.service.discount;

import org.springframework.stereotype.Component;

@Component
public class FixedAmountDiscountStrategy implements DiscountStrategy{
    private final int discountAmount = 500;

    public FixedAmountDiscountStrategy() {

    }

    @Override
    public int applyDiscount(int price, int count) {
        int total = price * count;
        return total > discountAmount ? total - discountAmount : 0;//총 금액이 할인 금액보다 적은 경우 처리
    }
}
