package com.example.ecommerce.service;

import com.example.ecommerce.dto.CartOrderForm;
import com.example.ecommerce.entity.*;
import com.example.ecommerce.exception.NotEnoughStockException;
import com.example.ecommerce.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartItemRepository cartItemRepository;
    private final WrappingItemRepository wrappingItemRepository;
    private final WrappingMaterialInfoRepository wrappingMaterialInfoRepository;
    
    //단일 아이템만 바로 주문
    public Long order(Long memberId, Long itemId, int count) {
        //해당 itemId를 통해 item 조회
        Item findItem = itemRepository.findById(itemId).orElse(null);
        Member findMember = memberRepository.findById(memberId).orElse(null);
        int orderPrice = findItem.getPrice() * count;

        List<OrderItem> orderItemList = new ArrayList<>();

        OrderItem orderItem = OrderItem.createSingleOrderItem(findItem, count, orderPrice);//현재 재고 수량보다 많은 주문의 경우 exception 발생
        orderItemList.add(orderItem);
        Order order = Order.createOrder(findMember, orderItemList);

        Order savedOrder = orderRepository.save(order);
        return savedOrder.getId();
        //포장지, 주문 상태 디자인 패턴 적용해보기, 어떻게 관리할지 고민하기
    }

    public Long orders(Long memberId, CartOrderForm cartOrderForm) {
        List<OrderItem> orderItemList = new ArrayList<>();
        Member findMember = memberRepository.findById(memberId).orElse(null);

        List<CartOrderForm.OrderForm> orderFormList = cartOrderForm.getOrderList();
        for (CartOrderForm.OrderForm orderForm : orderFormList) {
            CartItem cartItem = cartItemRepository.findById(orderForm.itemId()).orElse(null);
            Item findItem = cartItem.getItem();
            int orderPrice = findItem.getPrice() * orderForm.count();
            
            if(orderForm.count() > findItem.getStockQuantity()) {
                throw new NotEnoughStockException("상품 재고가 부족합니다!");
            }
            findItem.updateStock(orderForm.count());//주문된 수량 만큼 재고 수량에서 차감

            List<Long> wrappingMaterialList = orderForm.wrappingMaterialList();

            System.out.println(wrappingMaterialList);
            //첫번째 포장 옵션이 없음인 경우 포장 없이 주문된 아이템
            if(wrappingMaterialList.isEmpty()) {
                OrderItem orderItem = OrderItem.builder()
                        .item(findItem)
                        .itemsPrice(orderPrice)
                        .count(orderForm.count())
                        .wrappingOption(false)
                        .build();
                orderItemList.add(orderItem);

                int layer = 1;
                for (Long materialId : wrappingMaterialList) {
                    WrappingMaterialInfo wrappingMaterial = wrappingMaterialInfoRepository.findById(materialId).orElse(null);
                    WrappingItem wrappingItem = WrappingItem.createWrappingItem(orderItem, wrappingMaterial, layer);
                    wrappingItemRepository.save(wrappingItem);
                    layer++;
                }

            }

            else {
                OrderItem orderItem = OrderItem.builder()
                        .item(findItem)
                        .itemsPrice(orderPrice)
                        .count(orderForm.count())
                        .wrappingOption(true)
                        .build();
                orderItemList.add(orderItem);

                AtomicInteger layer = new AtomicInteger(1);

                wrappingMaterialList.stream()
                        .map(materialId -> wrappingMaterialInfoRepository.findById(materialId).orElse(null))
                        .map(wrappingItem -> WrappingItem.createWrappingItem(orderItem, wrappingItem, layer.getAndIncrement()))
                        .forEach(wrappingItemRepository::save);
            }
        }

        Order order = Order.createOrder(findMember, orderItemList);
        Order savedOrder = orderRepository.save(order);
        return savedOrder.getId();
    }
}
