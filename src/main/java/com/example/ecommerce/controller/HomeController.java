package com.example.ecommerce.controller;

import com.example.ecommerce.entity.Item;
import com.example.ecommerce.service.ItemService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
@Slf4j
@RequestMapping()
public class HomeController {
    private final ItemService itemService;

    @GetMapping
    public String homePage(Model model, HttpServletRequest request) {
        List<Item> items = itemService.findItems();
        if (items == null) {
            items = new ArrayList<>();
        }
        model.addAttribute("items", items);

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            log.info("home");
            return "home";
        }

        log.info("userHome");
        return "userHome";
    }

    @ResponseBody
    @GetMapping("/image/{filename}")
    public Resource getImage(@PathVariable String filename) throws MalformedURLException {
        // 파일 저장 경로
        String uploadDir = itemService.getUploadDir();

        // 파일 경로 생성
        Path filePath = Paths.get(uploadDir).resolve(filename);

        // UrlResource를 통해 파일 반환
        return new UrlResource(filePath.toUri());
    }
}
