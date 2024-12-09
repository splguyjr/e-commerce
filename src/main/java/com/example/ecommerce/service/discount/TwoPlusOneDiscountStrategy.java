package com.example.ecommerce.service.discount;

import org.springframework.stereotype.Component;

@Component
public class TwoPlusOneDiscountStrategy implements DiscountStrategy{
    public TwoPlusOneDiscountStrategy() {
    }

    @Override
    public int applyDiscount(int price, int count) {
        if (count >= 2) {
            int freeItemCount = count / 3;
            int discountPrice = freeItemCount * price;
            return (count * price) - discountPrice;
        }
        return count * price;
    }
}
