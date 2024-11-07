package com.securityModel.Services;



import com.securityModel.modele.Panier;

import java.util.HashMap;

public interface PanierService {
    HashMap<String, String> addProductToPanier(int productId, int quantity);
    HashMap<String, String> removeProductFromPanier(int productId);
    Panier getPanierActive();
}
