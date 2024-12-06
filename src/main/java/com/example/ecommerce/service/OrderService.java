package com.example.ecommerce.service;

import com.example.ecommerce.entity.Item;
import com.example.ecommerce.entity.Member;
import com.example.ecommerce.entity.Order;
import com.example.ecommerce.entity.OrderItem;
import com.example.ecommerce.repository.ItemRepository;
import com.example.ecommerce.repository.MemberRepository;
import com.example.ecommerce.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

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
}
