package com.example.ecommerce.service;

import com.example.ecommerce.dto.CartOrderForm;
import com.example.ecommerce.dto.MyPageForm;
import com.example.ecommerce.entity.*;
import com.example.ecommerce.exception.NotEnoughStockException;
import com.example.ecommerce.repository.*;
import com.example.ecommerce.service.discount.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartItemRepository cartItemRepository;
    private final WrappingItemRepository wrappingItemRepository;
    private final WrappingMaterialInfoRepository wrappingMaterialInfoRepository;
    private DiscountStrategy discountStrategy;

    public OrderService(OrderRepository orderRepository, ItemRepository itemRepository, MemberRepository memberRepository,
                        CartItemRepository cartItemRepository, WrappingItemRepository wrappingItemRepository,
                        WrappingMaterialInfoRepository wrappingMaterialInfoRepository) {
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
        this.memberRepository = memberRepository;
        this.cartItemRepository = cartItemRepository;
        this.wrappingItemRepository = wrappingItemRepository;
        this.wrappingMaterialInfoRepository = wrappingMaterialInfoRepository;

    }

    @PostConstruct
    public void setDiscountStrategy() {
        DiscountStrategyFactory factory = new PercentageDiscountStrategyFactory();
        this.discountStrategy = factory.get();
    }

    //단일 아이템만 바로 주문
    public Long order(Long memberId, Long itemId, int count) {
        //해당 itemId를 통해 item 조회
        Item findItem = itemRepository.findById(itemId).orElse(null);
        Member findMember = memberRepository.findById(memberId).orElse(null);
//        int orderPrice = findItem.getPrice() * count;
        int orderPrice = discountStrategy.applyDiscount(findItem.getPrice(), count);

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


//    public record OrderInfo(String itemName, String imageUrl, int itemCount, int itemPrice, int totalPrice) {}
//    public record MyPageInfo(MyPageForm.OrderInfo orderInfo, LocalDateTime orderDate, OrderStatus orderStatus) {}
//    private List<MyPageForm.MyPageInfo> myPageInfoList;
public List<MyPageForm> getMyPageInfo(Long memberId) {
    Member member = memberRepository.findById(memberId).orElse(null);
    List<Order> orderList = orderRepository.findByMemberId(member.getId());

    List<MyPageForm> myPageFormList = new ArrayList<>();

    for (Order order : orderList) {
        LocalDateTime orderDate = order.getOrderDate(); // 주문일자
        OrderStatus orderStatus = order.getOrderStatus(); // 배송상황
        List<OrderItem> orderItems = order.getOrderItems(); // 주문 아이템들

        int totalPrice = 0;
        List<MyPageForm.MyPageInfo> myPageInfos = new ArrayList<>();

        // OrderItem마다 OrderInfo만 생성하고, 나머지 정보는 for문 밖에서 처리
        for (OrderItem orderItem : orderItems) {
            Item item = orderItem.getItem();
            String itemName = item.getName(); // 아이템 이름
            String imageUrl = item.getImageUrl(); // 아이템 이미지
            int itemPrice = item.getPrice();//아이템 가격
            int itemCount = orderItem.getCount(); // 아이템 주문 수량
            int itemsPrice = orderItem.getItemsPrice(); // 아이템별 총 가격
            totalPrice += discountStrategy.applyDiscount(itemPrice, itemCount); // 주문 총 가격
            MyPageForm.OrderInfo orderInfo = MyPageForm.OrderInfo.builder()
                    .itemName(itemName)
                    .imageUrl(imageUrl)
                    .itemCount(itemCount)
                    .itemPrice(itemsPrice)
                    .build();

            // OrderInfo만 생성
            MyPageForm.MyPageInfo myPageInfo = MyPageForm.MyPageInfo.builder()
                    .orderInfo(orderInfo)
                    .build();

            myPageInfos.add(myPageInfo);
        }

        // for문 밖에서 orderStatus, orderDate, totalPrice 설정
        for (int i = 0; i < myPageInfos.size(); i++) {
            MyPageForm.MyPageInfo myPageInfo = myPageInfos.get(i);
            // orderStatus, orderDate, totalPrice 설정
            myPageInfos.set(i, myPageInfo.toBuilder()
                    .orderStatus(orderStatus)
                    .orderDate(orderDate)
                    .totalPrice(totalPrice)
                    .build());
        }

        // 최종적으로 MyPageForm을 추가
        MyPageForm myPageForm = MyPageForm.builder()
                .myPageInfoList(myPageInfos)
                .build();

        myPageFormList.add(myPageForm);
    }

    return myPageFormList;
}

    public void proceedOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found by orderId"));
        order.proceed();
        orderRepository.save(order);
    }

    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found by orderId"));
        order.cancel();
        orderRepository.save(order);
    }


}
