package com.example.ecommerce.entity;

import com.example.ecommerce.exception.NotEnoughStockException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    private String description;

    private String imageUrl;

    @Builder
    private Item(String name, int price, int stockQuantity, String description, String imageUrl) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public static Item createItem(String name, int price, int stockQuantity, String description, String imageUrl) {
        return new Item(name, price, stockQuantity, description, imageUrl);
    }

    public void updateStock(int count) {
        int leftQuantity = stockQuantity - count;//현재 재고 수량 - 주문 수량
        if (leftQuantity < 0) {
            throw new NotEnoughStockException("상품 재고가 부족합니다!");
        }
        stockQuantity = leftQuantity;
    }

    //나중에 필요한 setter 정의하기 ex) update
}
