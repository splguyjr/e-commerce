package com.example.ecommerce.service.discount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public abstract class DiscountStrategyFactory {

    public final DiscountStrategy get() {
        DiscountStrategy discountStrategy = createStrategy();
        return discountStrategy;
    }

    protected abstract DiscountStrategy createStrategy();
}
