package com.securityModel.modele;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.List;

@Entity
@Table(name="orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private int qte_total;
    private double price_total;
    private boolean state;
    @ElementCollection
    private List<String> messages;

    public void setPrice_total(double price_total) {
        this.price_total = price_total;
    }

    @ManyToOne
    @JsonIgnoreProperties("orders")
    @JoinColumn(name="customer_id")
    private Customer customer;

    @ManyToOne
    @JsonIgnoreProperties("order")
    @JoinColumn(name="driver_id")
    private Driver driver;


    @ManyToMany
    @JsonIgnoreProperties("orders")
    @JoinTable(name = "Order_product",
    joinColumns = @JoinColumn(name="order_id"),
    inverseJoinColumns = @JoinColumn(name="product_id"))
    private List<Product> products;

    @ManyToOne
    @JsonIgnoreProperties("orders")
    @JoinColumn(name = "user_id")
    private User user;


    @OneToOne
    @JsonIgnoreProperties("order")
    @JoinColumn(name = "panier_id")
    private Panier panier;

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
}
