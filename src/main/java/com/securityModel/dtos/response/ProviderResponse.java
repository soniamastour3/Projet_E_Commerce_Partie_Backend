package com.securityModel.dtos.response;


import com.securityModel.dtos.request.ProviderRequest;
import com.securityModel.modele.Provider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProviderResponse {
    private Long id;
    private String firstname;
    private String lastname;
    private String phone;
    private String email;
    private String password;
    private String username;
    private String company;
    private String image;

    public static ProviderResponse fromEntity(Provider entity) {
        ProviderResponse providerResponse = new ProviderResponse();
        BeanUtils.copyProperties(entity, providerResponse);
        return providerResponse;
    }

    public static Provider toEntity(ProviderRequest providerResponse) {
        Provider pr = new Provider();
        BeanUtils.copyProperties(providerResponse, pr);
        return pr;

    }
}
