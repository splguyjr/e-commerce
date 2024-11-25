package com.example.ecommerce.controller;


import com.example.ecommerce.dto.ItemForm;
import com.example.ecommerce.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@RequiredArgsConstructor
@Controller
@Slf4j
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/register")
    public String registerItemForm(Model model) {
        model.addAttribute("itemForm", new ItemForm());
        return "item/itemRegisterForm";
    }

    @PostMapping("/register")
    public String registerItem(@Valid @ModelAttribute("itemForm") ItemForm itemForm, BindingResult bindingResult, Model model) throws IOException {
        if (bindingResult.hasErrors()) return "item/itemRegisterForm";

        if (itemForm.getImage().isEmpty()) {
            model.addAttribute("errorMessage", "상품 사진을 등록해주세요!");
            return "item/itemRegisterForm";
        }

        itemService.registerItem(itemForm);

        return "redirect:/userHome";
    }



}
