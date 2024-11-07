package com.securityModel.dtos.response;


import com.securityModel.dtos.request.ClientRequest;
import com.securityModel.modele.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponse {
    private Long id;
    private String firstname;
    private String lastname;
    private String phone;
    private String email;
    private String password;
    private String username;
    private String localization;

    public static ClientResponse fromEntity(Customer entity) {
        ClientResponse clientResponse = new ClientResponse();
        BeanUtils.copyProperties(entity, clientResponse);
        return clientResponse;
    }

    public static Customer toEntity(ClientRequest clientResponse) {
        Customer cl = new Customer();
        BeanUtils.copyProperties(clientResponse, cl);
        return cl;

    }
}
