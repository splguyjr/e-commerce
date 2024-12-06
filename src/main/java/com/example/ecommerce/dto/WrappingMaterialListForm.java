package com.example.ecommerce.dto;

import com.example.ecommerce.entity.WrappingMaterialInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class WrappingMaterialListForm {

    @Builder
    public record WrappingMaterial(Long materialId, String materialName, int cost) {}

    private List<WrappingMaterial> wrappingMaterialList;
    public static WrappingMaterial from(WrappingMaterialInfo wrappingMaterialInfo) {
        return WrappingMaterial.builder()
                .materialId(wrappingMaterialInfo.getMaterialId())
                .materialName(wrappingMaterialInfo.getMaterialName())
                .cost(wrappingMaterialInfo.getCost())
                .build();
    }
}
