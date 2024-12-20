package com.example.ecommerce.service;

import com.example.ecommerce.dto.CartItemForm;
import com.example.ecommerce.dto.WrappingMaterialListForm;
import com.example.ecommerce.entity.*;
import com.example.ecommerce.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;
    private final CartItemRepository cartItemRepository;
    private final WrappingMaterialInfoRepository wrappingMaterialInfoRepository;

    public Long addCart(Long memberId, Long itemId, int count) {
        Member member = memberRepository.findById(memberId).get();
        Cart cart = cartRepository.findByMemberId(memberId).orElse(null);
        Item item = itemRepository.findById(itemId).get();

        //장바구니 없으면 생성
        if (cart == null) {
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        //멤버 id를 가지고 찾은 Cart의 id, 현재 장바구니에 추가하려는 Item의 id를 받아 해당하는 카트의 아이템이 존재하는지 확인
        CartItem cartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), itemId).orElse(null);

        //장바구니에 상품이 존재하지 않는 경우
        if (cartItem == null) {
            cartItem = CartItem.builder()
                    .count(count)
                    .cart(cart)
                    .item(item)
                    .build();
            CartItem savedCartItem = cartItemRepository.save(cartItem);
            return savedCartItem.getId();
        }

        //장바구니에 상품이 존재하는 경우, JPA의 변경 감지로 count값 update
        cartItem.changeCount(count);
        return cartItem.getId();
    }

    @Transactional(readOnly = true)
    public List<CartItemForm> findCartItems(Long memberId) {
        Cart cart = cartRepository.findByMemberId(memberId).orElseThrow(EntityNotFoundException::new);//로그인 이후 한번도 장바구니 추가를 안한 경우 Cart가 생성되지 않았을 수 있음
        List<CartItem> cartItems = cartItemRepository.findAllByCartId(cart.getId());
        List<CartItemForm> cartItemForms = cartItems.stream()
                .map(CartItemForm::from)
                .collect(Collectors.toList());

        return cartItemForms;
    }

    @Transactional(readOnly = true)
    public CartItem findCartItem(Long cartItemId) {
        return cartItemRepository.findById(cartItemId).orElse(null);
    }

    public void deleteCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElse(null);

        if (cartItem != null) {
            cartItemRepository.delete(cartItem);
        }
    }

    public WrappingMaterialListForm getWrappingMaterialList() {
        List<WrappingMaterialInfo> wrappingMaterialInfoList = wrappingMaterialInfoRepository.findAll();

        List<WrappingMaterialListForm.WrappingMaterial> wrappingMaterialList = wrappingMaterialInfoList.stream()
                .map(WrappingMaterialListForm::from)
                .collect(Collectors.toList());

        WrappingMaterialListForm wrappingMaterialListForm = WrappingMaterialListForm.builder()
                .wrappingMaterialList(wrappingMaterialList)
                .build();

        return wrappingMaterialListForm;
    }

}
