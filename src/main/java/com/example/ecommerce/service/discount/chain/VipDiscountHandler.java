package com.example.ecommerce.service.discount.chain;

import com.example.ecommerce.entity.Grade;
import com.example.ecommerce.entity.Member;
import com.example.ecommerce.entity.Order;
import org.springframework.stereotype.Component;

//Vip인 경우 5퍼센트 일괄 할인 적용
@Component
public class VipDiscountHandler extends DiscountHandler{
    @Override
    protected int calculateDiscount(int price, int count, Order order) {
        Member member = order.getMember();
        int discountPercentage = (member.getGrade() == Grade.VIP) ? 5 : 0;

        int discountAmount = price * count * discountPercentage / 100;
        System.out.println("vip 할인 양 : " + discountAmount);
        return discountAmount;
    }
}
