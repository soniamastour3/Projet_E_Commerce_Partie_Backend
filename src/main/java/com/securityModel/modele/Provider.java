package com.securityModel.modele;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.securityModel.models.Role;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="providers")
public class Provider extends User {


    private String company;


    @OneToMany(mappedBy="provider")
    @JsonIgnoreProperties("provider")
    private Collection<Product> products;

    public Provider() {
        super();
    }




    public Collection<Product> getProducts() {
        return products;
    }

    public void setProducts(Collection<Product> products) {
        this.products = products;
    }

    public Provider(String username, String email, String password, String company) {
        super(username, email, password);
        this.company = company;
    }



    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }


}