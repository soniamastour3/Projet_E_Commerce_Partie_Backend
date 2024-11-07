package com.securityModel.dtos.response;


import com.securityModel.dtos.request.ProductRequest;
import com.securityModel.modele.Gallery;
import com.securityModel.modele.Product;
import com.securityModel.modele.Provider;
import com.securityModel.modele.SubCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private int id;
    private String name;
    private String ret;
    private String color;
    private int qte;
    private int qte_en_stock;
    private double price;
    private String description;
    private List<ProductResponse> products;
    private String image;
    private List<Gallery> gallery;
    private SubCategory subCategory;
    private Provider provider;


    public ProductResponse(int id, String name, String ret, String color, int qte, int qteEnStock, double price, String description, String imageUrl) {
        this.id = id;
        this.name = name;
        this.ret = ret;
        this.color = color;
        this.qte = qte;
        this.qte_en_stock = qteEnStock;
        this.price = price;
        this.description = description;
        this.image = imageUrl;
    }

    public static ProductResponse fromEntity(Product entity) {
        ProductResponse productResponse = new ProductResponse();
        BeanUtils.copyProperties(entity, productResponse);
        return productResponse;
    }

    public static Product toEntity(ProductRequest productResponse) {
        Product p = new Product();
        BeanUtils.copyProperties(productResponse, p);
        return p;

    }


}
