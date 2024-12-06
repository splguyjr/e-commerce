package com.example.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "wrapping_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WrappingItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wrapping_id")
    private Long wrappingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id")
    private WrappingMaterialInfo wrappingMaterialInfo;

    @Column(name = "layer_level")
    private int layerLevel;

    private WrappingItem(OrderItem orderItem, WrappingMaterialInfo wrappingMaterialInfo, int layerLevel) {
        this.orderItem = orderItem;
        this.wrappingMaterialInfo = wrappingMaterialInfo;
        this.layerLevel = layerLevel;
    }

    public static WrappingItem createWrappingItem(OrderItem orderItem, WrappingMaterialInfo wrappingMaterialInfo, int layerLevel) {
        return new WrappingItem(orderItem,wrappingMaterialInfo, layerLevel);
    }
}
