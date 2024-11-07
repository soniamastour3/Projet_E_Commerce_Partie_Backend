package com.securityModel.Services;



import com.securityModel.dtos.request.ProductRequest;
import com.securityModel.dtos.request.ProviderRequest;
import com.securityModel.dtos.response.ProductResponse;
import com.securityModel.dtos.response.ProviderResponse;
import com.securityModel.payload.response.MessageResponse;
import jakarta.mail.MessagingException;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

public interface ProviderService {

    ProviderResponse updateproviderwithimage(ProviderRequest providerRequest, Long id, MultipartFile file);
    ProviderResponse createprovider(ProviderRequest provider);
    List<ProviderResponse> allprovider();
    ProviderResponse providerById(Long id);
    HashMap<String, String> deleteprovider(Long id);
}
