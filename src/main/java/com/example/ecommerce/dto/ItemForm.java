package com.example.ecommerce.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ItemForm {
    @NotEmpty(message = "상품 이름은 필수입니다")
    private String name;

    @NotNull(message = "상품 가격은 필수입니다.")
    private int price;

    @NotNull(message = "상품 재고 수량은 필수입니다.")
    private int stockQuantity;

    private String description;

    private MultipartFile image;
}
