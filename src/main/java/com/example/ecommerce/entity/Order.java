package com.example.ecommerce.entity;

import com.example.ecommerce.service.orderstate.OrderState;
import com.example.ecommerce.service.orderstate.PreparationState;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    private LocalDateTime orderDate;

    @Transient//JPA 매핑 대상 제외
    private OrderState orderState;

    private Order(Member member) {
        this.member = member;
        this.orderStatus = OrderStatus.Preparation;//초기 발송상태는 항상 입고 준비중
        this.orderDate = LocalDateTime.now();
        this.orderState = new PreparationState();
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    public void proceed() {
        this.orderState.proceed(this);
    }

    public void cancel() {
        this.orderState.cancel(this);
    }

    public String getOrderState() {
        return this.orderState.getStatus();
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.changeOrder(this);
    }

    public static Order createOrder(Member member, List<OrderItem> orderItems) {
        Order order = new Order(member);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        return order;
    }


}
