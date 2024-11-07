package com.securityModel.dtos.response;


import com.securityModel.dtos.request.GalleryRequest;
import com.securityModel.modele.Gallery;
import com.securityModel.modele.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GalleryResponse {
    private Long id;
    private String url_photo;
    private Product product;

    public static GalleryResponse fromEntity(Gallery entity) {
        GalleryResponse galloryResponse = new GalleryResponse();
        BeanUtils.copyProperties(entity, galloryResponse);
        return galloryResponse;
    }

    public static Gallery toEntity(GalleryRequest galleryResponse) {
        Gallery g = new Gallery();
        BeanUtils.copyProperties(galleryResponse, g);
        return g;
    }
}
