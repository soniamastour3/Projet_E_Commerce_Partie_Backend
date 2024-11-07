package com.securityModel.controllers;



import com.securityModel.Services.GalleryService;
import com.securityModel.dtos.request.GalleryRequest;
import com.securityModel.dtos.response.GalleryResponse;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/gallerys")
public class GalleryController {
    public final GalleryService galleryService;

    public GalleryController(GalleryService galleryService) {
        this.galleryService = galleryService;
    }

    @PostMapping("/save")
    public GalleryResponse addgallery(@RequestBody GalleryRequest galleryRequest) {
        return galleryService.createGallery(galleryRequest);
    }

    @GetMapping("/all")
    public List<GalleryResponse> AllGallery() {
        return galleryService.allGallery();
    }

    @GetMapping("/getone/{id}")
    public GalleryResponse gallerybyid(@PathVariable Long id) {
        return galleryService.galleryById(id);
    }

    @PutMapping("/update/{id}")
    public GalleryResponse updategallery(@RequestBody GalleryRequest galleryRequest, @PathVariable Long id) {
        return galleryService.updategallery(galleryRequest, id);
    }

    @DeleteMapping("/delete/{id}")
    public HashMap<String, String> deletegallery(@PathVariable Long id) {
        return galleryService.deletegallery(id);
    }

}
