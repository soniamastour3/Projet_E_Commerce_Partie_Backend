package com.securityModel.modele;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="wishlist")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WishList {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @ManyToMany
    @JsonIgnoreProperties("wishLists")
    @JoinTable(name = "wishList_product",
            joinColumns = @JoinColumn(name="wishList_id"),
            inverseJoinColumns = @JoinColumn(name="product_id"))
    private List<Product> products;

    @OneToOne
    @JsonIgnoreProperties("wishList")
    @JoinColumn(name="user_id")
    private User user;
}
