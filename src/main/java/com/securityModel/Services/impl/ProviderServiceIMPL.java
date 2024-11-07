package com.securityModel.Services.impl;


import com.securityModel.Services.ProviderService;
import com.securityModel.Utils.StoresService;
import com.securityModel.dtos.request.ProductRequest;
import com.securityModel.dtos.request.ProviderRequest;
import com.securityModel.dtos.response.ProductResponse;
import com.securityModel.dtos.response.ProviderResponse;
import com.securityModel.modele.Product;
import com.securityModel.modele.Provider;
import com.securityModel.models.ERole;
import com.securityModel.models.Role;
import com.securityModel.payload.response.MessageResponse;
import com.securityModel.repository.ProviderDao;
import com.securityModel.repository.RoleRepository;
import com.securityModel.repository.UserDao;
import jakarta.mail.MessagingException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProviderServiceIMPL implements ProviderService {

    private final ProviderDao providerDaoconst;
    private final UserDao userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final StoresService storesService;

    public ProviderServiceIMPL(ProviderDao providerDaoconst, UserDao userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, StoresService storesService) {
        this.providerDaoconst = providerDaoconst;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.storesService = storesService;
    }

    @Override
    public ProviderResponse createprovider(ProviderRequest provider) {
        Provider pr= ProviderResponse.toEntity(provider);
        Provider savedprovider= providerDaoconst.save(pr);
        return ProviderResponse.fromEntity(savedprovider);
    }

//    @Override
//    public ProviderResponse createproviderwithimage(ProviderRequest providerRequest, MultipartFile file) {
//        Provider p= ProviderRequest.toEntity(providerRequest);
//        String img = storesService.store(file);
//        p.setImage(img);
//        Provider savedprovider= providerDaoconst.save(p);
//        return ProductResponse.fromEntity(savedprovider);
//    }
    @Override
    public List<ProviderResponse> allprovider() {
        return providerDaoconst.findAll().stream()
                .map(ProviderResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public ProviderResponse providerById(Long id) {
        return providerDaoconst.findById(id)
                .map(ProviderResponse::fromEntity)
                .orElseThrow(() ->new RuntimeException("Provider not found with this id:" +id));
    }



    @Override
    public ProviderResponse updateproviderwithimage(ProviderRequest providerRequest, Long id, MultipartFile file) {
        Provider provider=providerDaoconst.findById(id).orElseThrow(()->
                new RuntimeException("Provider not found with this id:" +id));
        if (provider != null) {
            Provider p = ProviderResponse.toEntity(providerRequest);
            p.setId(id);
            // Si le nom de p est null, on utilise le nom du produit. Sinon, on garde le nom actuel de p.
            String img = storesService.store(file);
            p.setImage(img);
            Provider savedprovider = providerDaoconst.save(p);
            return ProviderResponse.fromEntity(savedprovider);
        }else{
            return null;
        }
    }

    @Override
    public HashMap<String, String> deleteprovider(Long id) {
        HashMap message =new HashMap<>();
        Provider pr = providerDaoconst.findById(id).orElse(null);
        if (pr != null) {
            try {
                providerDaoconst.deleteById(id);
                message.put("message", "Provider deleted successfully");
            }catch (Exception a){
                message.put("message", a.getMessage());
            }
        }else{
            message.put("message", "Provider not found: " +id);
        }
        return message;
    }


}
