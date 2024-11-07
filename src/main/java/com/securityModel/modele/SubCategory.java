package com.securityModel.modele;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="subCategorys")
public class SubCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

@ManyToOne
@JoinColumn(name="categorie_id", nullable = false)
@JsonIgnoreProperties("subCategories")
private Category category;

@OneToMany(mappedBy="subCategory")
@JsonIgnoreProperties("subCategory")
private Collection<Product> products;



}
