package com.securityModel.dtos.request;


import com.securityModel.modele.Customer;
import com.securityModel.modele.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private Long id;
    private String description;
    private int qte_total;
    private double price_total;
    private boolean state;
    private List<Integer> productIds;
    private Customer customer;
    private List<Product> products = new ArrayList<>();

    public void addproduct(Product product) {
        if (product != null) {
            this.products.add(product);
        }
    }

    public void setClient(Customer customer) {
        this.customer = customer;
    }


}
