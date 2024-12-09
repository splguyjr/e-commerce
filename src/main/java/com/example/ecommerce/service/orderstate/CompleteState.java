package com.example.ecommerce.service.orderstate;

import com.example.ecommerce.entity.Order;

public class CompleteState implements OrderState{
    @Override
    public void proceed(Order order) {
        throw new IllegalStateException("Order is already complete");
    }

    @Override
    public void cancel(Order order) {
        throw new IllegalStateException("Cannot cancel a completed order");
    }

    @Override
    public String getStatus() {
        return "Complete";
    }
}
