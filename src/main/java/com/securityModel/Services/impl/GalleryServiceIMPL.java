package com.securityModel.Services.impl;


import com.securityModel.Services.GalleryService;
import com.securityModel.dtos.request.GalleryRequest;
import com.securityModel.dtos.response.GalleryResponse;
import com.securityModel.modele.Gallery;
import com.securityModel.repository.GalleryDao;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GalleryServiceIMPL implements GalleryService {
    private final GalleryDao galleryDaoconst;

    public GalleryServiceIMPL(GalleryDao galleryDaoconst) {
        this.galleryDaoconst = galleryDaoconst;
    }

    @Override
    public GalleryResponse createGallery(GalleryRequest gallery){
        Gallery g=GalleryResponse.toEntity(gallery);
        Gallery savedgallery= galleryDaoconst.save(g);
        return GalleryResponse.fromEntity(savedgallery);
    }

    @Override
    public List<GalleryResponse> allGallery(){
        return galleryDaoconst.findAll().stream()
                .map(GalleryResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public GalleryResponse galleryById(Long id) {
        return galleryDaoconst.findById(id)
                .map(GalleryResponse::fromEntity)
                .orElseThrow(() ->new RuntimeException("Gallery not found with this id:" +id));    }

    @Override
    public GalleryResponse updategallery(GalleryRequest galleryRequest, Long id) {
        Gallery gallery=galleryDaoconst.findById(id).orElseThrow(()->
                new RuntimeException("Gallery not found with this id:" +id));
        if (gallery != null) {
            Gallery g = GalleryResponse.toEntity(galleryRequest);
            g.setId(id);
            g.setUrl_photo(g.getUrl_photo() == null ? gallery.getUrl_photo() : g.getUrl_photo());
            Gallery savedgallery = galleryDaoconst.save(g);
            return GalleryResponse.fromEntity(savedgallery);
        }else{
            return null;
        }
    }

    @Override
    public HashMap<String, String> deletegallery(Long id) {
        HashMap message =new HashMap<>();
        Gallery g = galleryDaoconst.findById(id).orElse(null);
        if (g != null) {
            try {
                galleryDaoconst.deleteById(id);
                message.put("message", "Gallery deleted successfully");
            }catch (Exception a){
                message.put("message", a.getMessage());
            }
        }else{
            message.put("message", "Gallery not found: " +id);
        }
        return message;
    }
    }
