package com.securityModel.controllers;


import com.securityModel.Services.ClientService;
import com.securityModel.dtos.request.ClientRequest;
import com.securityModel.dtos.response.ClientResponse;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {
    public final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/save")
    public ClientResponse addClient(@RequestBody ClientRequest clientRequest) {
        return clientService.createclient(clientRequest);
    }

    @GetMapping("/all")
    public List<ClientResponse> AllClient() {
        return clientService.allclient();
    }

    @GetMapping("/getone/{id}")
    public ClientResponse clientbyid(@PathVariable Long id) {
        return clientService.clientById(id);
    }

    @PutMapping("/update/{id}")
    public ClientResponse updateclient(@RequestBody ClientRequest clientRequest, @PathVariable Long id) {
        return clientService.updateclient(clientRequest, id);
    }

    @DeleteMapping("/delete/{id}")
    public HashMap<String, String> deleteclient(@PathVariable Long id) {
        return clientService.deleteclient(id);
    }

}
