package com.example.ecommerce.service.orderstate;

import com.example.ecommerce.entity.Order;

public interface OrderState {
    void proceed(Order order);
    void cancel(Order order);
    String getStatus();
}
