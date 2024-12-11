package com.example.ecommerce.service.discount.chain;

import com.example.ecommerce.entity.Order;
import org.springframework.stereotype.Component;


//2개 구매 시 1개는 증정하는 할인전략
@Component
public class TwoPlusOneDiscountHandler extends DiscountHandler{
    @Override
    protected int calculateDiscount(int price, int count, Order order) {
        if (count >= 2) {
            int freeCount = count / 3;
            int discountAmount = freeCount * price;
            System.out.println("2+1 할인 금액 : " + discountAmount);
            return discountAmount;
        }
        return 0;
    }
}
