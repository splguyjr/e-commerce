package com.example.ecommerce.service.orderstate;

import com.example.ecommerce.entity.Order;

public class PreparationState implements OrderState{
    @Override
    public void proceed(Order order) {
        order.setOrderState(new ShippingState());
    }

    @Override
    public void cancel(Order order) {
        order.setOrderState(new CancelState());
    }

    @Override
    public String getStatus() {
        return "Preparation";
    }
}
