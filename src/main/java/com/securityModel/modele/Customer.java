package com.securityModel.modele;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.securityModel.models.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="clients")
@Getter @Setter
public class Customer extends User {

    private String location;



    public Customer() {
    }

    public Customer(String username, String email, String password, String location) {
        super(username, email, password);
        this.location = location;
    }



    @OneToMany(mappedBy="customer")
    @JsonIgnoreProperties("customer")
    private Collection<Order> orders;


}