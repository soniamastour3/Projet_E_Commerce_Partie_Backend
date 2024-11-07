package com.securityModel.dtos.request;


import com.securityModel.modele.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GalleryRequest {
    private Long id;
    private String url_photo;
    private Product product;
}
