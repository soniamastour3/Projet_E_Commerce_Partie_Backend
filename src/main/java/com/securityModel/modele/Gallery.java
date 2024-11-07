package com.securityModel.modele;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name="galleries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Gallery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url_photo;
    /*@ElementCollection
    private List<String> images= new ArrayList<>();

    public void setImages(List<String> images) {
        this.images = images;
    }*/

    @ManyToOne
    @JsonIgnoreProperties("galleries")
    @JoinColumn(name="product_id")
    private Product product;
}
