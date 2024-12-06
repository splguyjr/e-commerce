package com.example.ecommerce.controller;

import com.example.ecommerce.dto.CartForm;
import com.example.ecommerce.dto.CartOrderForm;
import com.example.ecommerce.entity.Member;
import com.example.ecommerce.exception.NotEnoughStockException;
import com.example.ecommerce.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order")
    @ResponseBody
    public ResponseEntity<String> order(@ModelAttribute("cartForm") CartForm cartForm, HttpServletRequest request) {
        Member member = CartController.getMember(request);

        if (member == null) {
            return new ResponseEntity<String>("로그인이 필요한 서비스입니다", HttpStatus.UNAUTHORIZED);
        }

        try {
            orderService.order(member.getId(), cartForm.getItemId(), cartForm.getCount());
        } catch (NotEnoughStockException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok("주문에 성공하였습니다");
    }

    @PostMapping("/orders")
    @ResponseBody
    public ResponseEntity<String> orders(@RequestBody CartOrderForm cartOrderForm, HttpServletRequest request) {
        //장바구니에서 아무 상품도 체크하지 않을 경우
        if (cartOrderForm.getOrderList() == null) {
            return new ResponseEntity<>("하나 이상의 상품을 주문하셔야 합니다.", HttpStatus.FORBIDDEN);
        }

        Member member = CartController.getMember(request);
        if (member == null) {
            return new ResponseEntity<>("로그인이 필요한 서비스입니다", HttpStatus.UNAUTHORIZED);
        }

        try {
            orderService.orders(member.getId(), cartOrderForm);
        } catch (NotEnoughStockException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok("주문에 성공하였습니다");
    }
}
