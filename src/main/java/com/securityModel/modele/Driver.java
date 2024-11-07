package com.securityModel.modele;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Entity
@Table(name="drivers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Driver extends User {
    /*@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;*/
    private String adress;

    @OneToMany(mappedBy = "driver")
    @JsonIgnoreProperties("driver")
    private Collection<Order> orders;

}
