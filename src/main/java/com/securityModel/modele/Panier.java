package com.securityModel.modele;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Entity
@Table(name = "paniers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Panier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int qteTotal;
    private double pricetotaltotal;
    @Column(name = "is_active")
    private boolean isActive;

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }






    public int getQteTotal() {
        return qteTotal;
    }

    public void setQte_user(int qte) {
        qte = qte;
    }

    public void setQteTotal(int qteTotal) {
        this.qteTotal = qteTotal;
    }

    @ManyToMany
    @JsonIgnoreProperties("paniers")
    @JoinTable(name = "panier_product",
            joinColumns = @JoinColumn(name = "panier_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> products = new ArrayList<>();

    @OneToOne(mappedBy = "panier")
    @JsonIgnoreProperties("panier")
    private Order order;


    public void calculateTotalPrice() {
        double totalPrice = this.products.stream().mapToDouble(product-> product.getPrice() * product.getQte_user()).sum();
        this.pricetotaltotal= totalPrice;
    }
    @ElementCollection
    @CollectionTable(name = "panier_items", joinColumns = @JoinColumn(name = "panier_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<Long, Integer> items = new HashMap<>();

    public int getQuantityByProductId(Long productId) {
        // Get the quantity from the map; default to 0 if the product ID is not found
        return items.getOrDefault(productId, 0);
    }
}
