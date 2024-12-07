package com.example.ecommerce.dto;

import com.example.ecommerce.entity.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
@Value
public class MyPageForm {

    @Builder
    public record OrderInfo(String itemName, String imageUrl, int itemCount, int itemPrice) {}
    @Builder(toBuilder = true)
    public record MyPageInfo(OrderInfo orderInfo, LocalDateTime orderDate, OrderStatus orderStatus, int totalPrice) {}

    private List<MyPageInfo> myPageInfoList;
}
