package com.example.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "order_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL, orphanRemoval = true) //부모-자식 관계에서 연관된 데이터를 함께 저장/삭제, 부모측에서 자식 엔티티 삭제 가능
    private List<WrappingItem> wrappingItems = new ArrayList<>();

    private int count;

    @Column(name = "items_price")
    private int itemsPrice;

    @Column(name = "wrapping_option")
    private Boolean wrappingOption;//포장 여부

    @Builder
    private OrderItem(Item item, int count, int itemsPrice) {
        this.item = item;
        this.count = count;
        this.itemsPrice = itemsPrice;
    }

    @Builder
    private OrderItem(Item item, int count, int itemsPrice, Boolean wrappingOption) {
        this.item = item;
        this.count = count;
        this.itemsPrice = itemsPrice;
        this.wrappingOption = wrappingOption;
    }

    public void changeOrder(Order order) {
        this.order = order;
    }

    public static OrderItem createSingleOrderItem(Item item, int count, int itemsPrice) {
        item.updateStock(count);
        return new OrderItem(item, count, itemsPrice);
    }

}
