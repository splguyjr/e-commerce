package com.example.ecommerce.controller;

import com.example.ecommerce.dto.CartDeleteForm;
import com.example.ecommerce.dto.CartForm;
import com.example.ecommerce.dto.CartItemForm;
import com.example.ecommerce.entity.CartItem;
import com.example.ecommerce.entity.Member;
import com.example.ecommerce.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    @GetMapping("")
    public String cartView(Model model, HttpServletRequest request) {
        Member member = getMember(request);
        List<CartItemForm> cartItems = cartService.findCartItems(member.getId());
        model.addAttribute("cartItemListForm", cartItems);

        return "cart/cartView";
    }



    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<String> addItem(@ModelAttribute("cartForm") CartForm cartForm, HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        //비로그인 상태에서 장바구니 추가 요청시
        if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            return new ResponseEntity<String>("로그인이 필요합니다.", HttpStatus.BAD_REQUEST);
        }

        //로그인 상태인 경우 세션에 저장되어있는 회원정보 가져옴.
        Member member = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);

        cartService.addCart(member.getId(), cartForm.getItemId(), cartForm.getCount());
        return ResponseEntity.ok("success");
    }

    @DeleteMapping("/delete")
    @ResponseBody
    public ResponseEntity<String> deleteItem(@RequestBody CartDeleteForm cartDeleteForm) {
        System.out.println(cartDeleteForm.getCartItemId());
        if (cartService.findCartItem(cartDeleteForm.getCartItemId()) == null) {
            return new ResponseEntity<String>("잘못된 요청입니다.", HttpStatus.NOT_FOUND);
        }

        cartService.deleteCartItem(cartDeleteForm.getCartItemId());
        return ResponseEntity.ok("success");
    }

    public static Member getMember(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            return null;
        }

        Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        return member;
    }
}
