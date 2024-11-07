package com.securityModel.Services.impl;

import com.securityModel.Services.ClientService;
import com.securityModel.dtos.request.ClientRequest;
import com.securityModel.dtos.response.ClientResponse;
import com.securityModel.modele.Customer;
import com.securityModel.repository.ClientDao;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceIMPL implements ClientService {
    private final ClientDao clientDaoconst;

    public ClientServiceIMPL(ClientDao clientDaoconst) {
        this.clientDaoconst = clientDaoconst;
    }

    @Override
    public ClientResponse createclient(ClientRequest customer){
        Customer cl=ClientResponse.toEntity(customer);
        Customer savedclient= clientDaoconst.save(cl);
        return ClientResponse.fromEntity(savedclient);
    }
    @Override
    public List<ClientResponse> allclient(){
        return clientDaoconst.findAll().stream()
                .map(ClientResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public ClientResponse clientById(Long id) {
        return clientDaoconst.findById(id)
                .map(ClientResponse::fromEntity)
                .orElseThrow(() ->new RuntimeException("PClient not found with this id:" +id));
    }

    @Override
    public ClientResponse updateclient(ClientRequest clientRequest, Long id) {
        Customer customer =clientDaoconst.findById(id).orElseThrow(()->
                new RuntimeException("Client not found with this id:" +id));
        if (customer != null) {
            Customer c = ClientResponse.toEntity(clientRequest);
            c.setId(id);
            c.setUsername(c.getUsername() == null ? customer.getUsername() : c.getUsername());
            Customer savedclient = clientDaoconst.save(c);
            return ClientResponse.fromEntity(savedclient);
        }else{
            return null;
        }
    }

    @Override
    public HashMap<String, String> deleteclient(Long id) {
        HashMap message =new HashMap<>();
        Customer c = clientDaoconst.findById(id).orElse(null);
        if (c != null) {
            try {
                clientDaoconst.deleteById(id);
                message.put("message", "Client deleted successfully");
            }catch (Exception a){
                message.put("message", a.getMessage());
            }
        }else{
            message.put("message", "Client not found: " +id);
        }
        return message;
    }
}
