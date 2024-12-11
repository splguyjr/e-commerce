package com.example.ecommerce.service;

import com.example.ecommerce.entity.Order;
import com.example.ecommerce.service.discount.chain.DiscountHandler;
import com.example.ecommerce.service.discount.chain.TwoPlusOneDiscountHandler;
import com.example.ecommerce.service.discount.chain.VipDiscountHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DiscountService {
    private final DiscountHandler discountChain;

    public DiscountService(TwoPlusOneDiscountHandler twoPlusOneDiscountHandler, VipDiscountHandler vipDiscountHandler) {
        twoPlusOneDiscountHandler.setNextHandler(vipDiscountHandler);
        this.discountChain = twoPlusOneDiscountHandler;
    }

    public int calculateFinalPrice(int price, int count, Order order) {
        int totalDiscount = discountChain.calculateTotalDiscount(price, count, order);
        int finalPrice = (price * count) - totalDiscount;
        return finalPrice;
    }

}
