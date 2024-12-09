package com.example.ecommerce.service.discount;

public class FixedAmountDiscountStrategyFactory extends DiscountStrategyFactory{
    @Override
    protected DiscountStrategy createStrategy() {
        return new FixedAmountDiscountStrategy();
    }
}
