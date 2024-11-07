package com.securityModel.dtos.request;

import com.securityModel.modele.Provider;
import com.securityModel.modele.SubCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    private int id;
    private String name;
    private String ret;
    private String color;
    private int qte;
    private int qte_en_stock;
    private double price;
    private String description;
    private SubCategory subCategory;
    private Provider provider;
    private String image;
}
