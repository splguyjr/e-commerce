package com.example.ecommerce.service.discount;

public class TwoPlusOneDiscountStrategyFactory extends DiscountStrategyFactory{
    @Override
    protected DiscountStrategy createStrategy() {
        return new TwoPlusOneDiscountStrategy();
    }
}
