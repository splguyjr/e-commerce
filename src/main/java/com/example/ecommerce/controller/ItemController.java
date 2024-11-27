package com.example.ecommerce.controller;


import com.example.ecommerce.dto.ItemForm;
import com.example.ecommerce.entity.Item;
import com.example.ecommerce.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

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

        Long itemId = itemService.registerItem(itemForm);
        Item item = itemService.findItem(itemId);
        log.info("user : {} item registration successful", item.getName());

        return "redirect:/";
    }

    @GetMapping("")
    public String getItemList(Model model) {
        List<Item> itemList = itemService.findItems();
        model.addAttribute("itemList", itemList);
        return "item/itemList";
    }
}
