package com.example.ecommerce.service.orderstate;

import com.example.ecommerce.entity.Order;

public class ShippingState implements OrderState{
    @Override
    public void proceed(Order order) {
        order.setOrderState(new CompleteState());
    }

    @Override
    public void cancel(Order order) {
        throw new IllegalStateException("Cannot cancel an order that is already shipping");
    }

    @Override
    public String getStatus() {
        return "Shipping";
    }
}
