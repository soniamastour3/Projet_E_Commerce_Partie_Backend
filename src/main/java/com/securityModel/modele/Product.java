package com.securityModel.modele;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;


@Entity
@Table (name="products")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Product {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String ret;
    private String color;
    private Integer qte;
    private Integer qte_en_stock;
    private Double price;
    private String description;
    private int nbreOrder = 0;
    private String image;
    private int quantity;
    private int qte_user;


    @ElementCollection
    private List<String> images;


    public void setImages(List<String> images) {
        this.images = images;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setNbreOrder(int nbreOrder) {
        this.nbreOrder = nbreOrder;
    }

    public int getNbreOrder() {
        return nbreOrder;
    }
    @ManyToOne
    @JsonIgnoreProperties("products")
    @JoinColumn(name="provider_id")
    private Provider provider;

    @ManyToOne
    @JsonIgnoreProperties("products")
    @JoinColumn(name="subCategory_id")
    private SubCategory subCategory;

    @OneToMany(mappedBy= "product")
    @JsonIgnoreProperties("product")
    private List<Gallery> galleries;


    @ManyToMany(mappedBy= "products")
    @JsonIgnoreProperties("products")
    private List<Order> orders;

    @ManyToMany(mappedBy= "products")
    @JsonIgnoreProperties("products")
    private List<WishList> wishLists;

    @ManyToMany(mappedBy= "products",cascade = CascadeType.ALL)
    @JsonIgnoreProperties("products")
    private List<Panier> paniers;
}