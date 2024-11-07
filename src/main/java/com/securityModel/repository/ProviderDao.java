package com.securityModel.repository;


import com.securityModel.dtos.request.ProviderRequest;
import com.securityModel.dtos.response.ProviderResponse;
import com.securityModel.modele.Provider;
import com.securityModel.modele.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface ProviderDao extends JpaRepository<Provider, Long> {
    Provider findByEmail(String email);
}
