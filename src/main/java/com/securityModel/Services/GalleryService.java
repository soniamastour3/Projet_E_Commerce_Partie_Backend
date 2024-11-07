package com.securityModel.Services;


import com.securityModel.dtos.request.GalleryRequest;
import com.securityModel.dtos.response.GalleryResponse;

import java.util.HashMap;
import java.util.List;

public interface GalleryService {
    GalleryResponse createGallery(GalleryRequest gallery);
    List<GalleryResponse> allGallery();
    GalleryResponse galleryById(Long id);
    GalleryResponse updategallery(GalleryRequest galleryRequest, Long id);
    HashMap<String, String> deletegallery(Long id);
}
