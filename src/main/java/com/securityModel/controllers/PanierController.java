package com.securityModel.controllers;



import com.securityModel.Services.PanierService;
import com.securityModel.dtos.response.PanierResponse;
import com.securityModel.modele.Panier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@CrossOrigin("*")
@RequestMapping("/paniers")
public class PanierController {

    private  final PanierService panierService;

    public PanierController(PanierService panierService) {
        this.panierService = panierService;
    }

    @PostMapping("/add/{productId}")
    public HashMap<String, String> addProductToCart( @PathVariable int productId,int quantity){
        return panierService.addProductToPanier(productId,quantity);
    }
    @DeleteMapping("/remove/{productId}")
    public HashMap<String, String> removeProductFromCart(@PathVariable int productId){
        return panierService.removeProductFromPanier(productId);
    }
    @GetMapping("/active")
    public ResponseEntity<PanierResponse> getActivePanier() {
        Panier activePanier = panierService.getPanierActive(); // Mettez à jour cette méthode pour ne pas nécessiter d'ID client
        PanierResponse response = PanierResponse.fromPanier(activePanier);
        if (activePanier != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
