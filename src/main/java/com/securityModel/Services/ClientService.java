package com.securityModel.Services;


import com.securityModel.dtos.request.ClientRequest;
import com.securityModel.dtos.response.ClientResponse;

import java.util.HashMap;
import java.util.List;

public interface ClientService {
    ClientResponse createclient(ClientRequest client);
    List<ClientResponse> allclient();
    ClientResponse clientById(Long id);
    ClientResponse updateclient(ClientRequest clientRequest, Long id);
    HashMap<String, String> deleteclient(Long id);
}
