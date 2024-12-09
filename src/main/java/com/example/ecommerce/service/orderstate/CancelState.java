package com.example.ecommerce.service.orderstate;

import com.example.ecommerce.entity.Order;

public class CancelState implements OrderState{
    @Override
    public void proceed(Order order) {
        throw new IllegalStateException("Cannot proceed a canceled order");
    }

    @Override
    public void cancel(Order order) {
        throw new IllegalStateException("Order is already canceled");
    }

    @Override
    public String getStatus() {
        return "Cancel";
    }
}
