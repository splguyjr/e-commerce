package com.example.ecommerce.service.discount;

public class PercentageDiscountStrategyFactory extends DiscountStrategyFactory{
    @Override
    protected DiscountStrategy createStrategy() {
        return new PercentageDiscountStrategy();
    }
}
