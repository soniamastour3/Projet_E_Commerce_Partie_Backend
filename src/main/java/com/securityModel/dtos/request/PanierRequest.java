package com.securityModel.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PanierRequest {
    private Long idPanier;
    private List<ProductToAdd> products;
    private boolean isActive;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductToAdd {
        private Long productId;
        private int quantity;
        private String name;
        private String ret;
        private String color;
        private Integer qte;
        private Integer qte_en_stock;
        private Double price;
        private String description;
        private int nbreOrder = 0;
        private String image;
        private boolean isActive;
    }
}