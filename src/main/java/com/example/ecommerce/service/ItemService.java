package com.example.ecommerce.service;

import com.example.ecommerce.dto.ItemForm;
import com.example.ecommerce.entity.Item;
import com.example.ecommerce.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public Long registerItem(ItemForm itemForm) throws IOException {
        try {
            MultipartFile image = itemForm.getImage();

            String originalFilename = image.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String savedFilename = UUID.randomUUID() + fileExtension;

            Path filePath = Paths.get(uploadDir, savedFilename);

            Files.createDirectories(filePath.getParent());
            image.transferTo(filePath.toFile());

            Item item = Item.createItem(itemForm.getName(),
                    itemForm.getPrice(),
                    itemForm.getStockQuantity(),
                    itemForm.getDescription(),
                    filePath.toString()
            );

            itemRepository.save(item);
            return item.getId();
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }
    }
}
