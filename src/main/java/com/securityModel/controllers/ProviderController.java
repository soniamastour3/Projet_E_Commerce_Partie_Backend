package com.securityModel.controllers;


import com.securityModel.Services.ProviderService;
import com.securityModel.dtos.request.ProductRequest;
import com.securityModel.dtos.request.ProviderRequest;
import com.securityModel.dtos.response.ProductResponse;
import com.securityModel.dtos.response.ProviderResponse;
import com.securityModel.security.services.AuthService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;


@RestController
@CrossOrigin("*")
@RequestMapping("/providers")
public class ProviderController {
    public final ProviderService providerService;
    private final AuthService authService;

    public ProviderController(ProviderService providerService, AuthService authService) {
        this.providerService = providerService;
        this.authService = authService;
    }

    @PostMapping("/save")
    public ProviderResponse addprovider(@RequestBody ProviderRequest providerRequest) {
        return providerService.createprovider(providerRequest);
    }

    @GetMapping("/all")
    public List<ProviderResponse> AllProduct() {
        return providerService.allprovider();
    }

    @GetMapping("/getone/{id}")
    public ProviderResponse providerbyid(@PathVariable Long id) {
        return providerService.providerById(id);
    }



    @DeleteMapping("/delete/{id}")
    public HashMap<String, String> deleteprovider(@PathVariable Long id) {
        return providerService.deleteprovider(id);
    }

    @PutMapping(value = "/updatewithimage/{id}", consumes = { "multipart/form-data" })
    public ProviderResponse updateproductwithimage(ProviderRequest providerRequest, @PathVariable Long id, MultipartFile file) {
        return providerService.updateproviderwithimage(providerRequest, id, file);
    }




}
