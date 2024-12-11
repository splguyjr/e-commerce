package com.example.ecommerce.service.discount.chain;

import com.example.ecommerce.entity.Order;

public abstract class DiscountHandler {
    private DiscountHandler nextHandler;

    public void setNextHandler(DiscountHandler discountHandler) {
        this.nextHandler = discountHandler;
    }

    public int calculateTotalDiscount(int price, int count, Order order) {
        int currentDiscount = calculateDiscount(price, count, order);

        if (nextHandler != null) {
            if(nextHandler instanceof VipDiscountHandler) { //% 할인을 적용하는 경우 이전까지의 할인 금액을 총 금액에서 먼저 뺌
                return currentDiscount + nextHandler.calculateTotalDiscount((price * count - currentDiscount) / count, count, order);
            }
            return currentDiscount + nextHandler.calculateTotalDiscount(price, count, order);//재귀적으로 다음 할인 전략의 할인 금액을 더함
        }

        //nextHandler가 없는 경우 할인 총 금액 반환
        return currentDiscount;
    }

    protected abstract int calculateDiscount(int price, int count, Order order);
}
