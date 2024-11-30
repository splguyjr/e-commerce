package com.example.ecommerce.repository;

import com.example.ecommerce.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartIdAndItemId(Long cartId, Long itemId);

    @Query("SELECT ci FROM CartItem ci JOIN FETCH ci.item WHERE ci.cart.id = :cartId")//eager방식으로 조회시 한번에 연관된 자식 엔티티까지 조회해옴
    List<CartItem> findAllByCartId(@Param("cartId")Long cartId);


}
