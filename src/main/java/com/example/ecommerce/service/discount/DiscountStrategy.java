package com.example.ecommerce.service.discount;

public interface DiscountStrategy {
    public abstract int applyDiscount(int price, int count);
}
