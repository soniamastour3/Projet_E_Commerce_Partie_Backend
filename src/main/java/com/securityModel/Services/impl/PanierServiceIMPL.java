package com.securityModel.Services.impl;


import com.securityModel.Services.PanierService;
import com.securityModel.modele.Panier;
import com.securityModel.modele.Product;
import com.securityModel.repository.ClientDao;
import com.securityModel.repository.PanierDao;
import com.securityModel.repository.ProductDao;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;


@Service
public class PanierServiceIMPL implements PanierService {
    private static final Logger logger = LoggerFactory.getLogger(PanierServiceIMPL.class);

    private final ProductDao productDao;
    private final PanierDao panierDao;
    private final ClientDao clientDao;



    public PanierServiceIMPL(ProductDao productDao, PanierDao panierDao, ClientDao clientDao) {

        this.productDao = productDao;
        this.panierDao = panierDao;
        this.clientDao = clientDao;
    }


//    @Override
//    public HashMap<String, String> addProductToPanier(Long productId, int quantity, Long panierId) {
//        HashMap<String, String> response = new HashMap<>();
//
//        // Récupérer le produit
//        Optional<Product> productOptional = productDao.findById(productId);
//        if (productOptional.isEmpty()) {
//            response.put("status", "error");
//            response.put("message", "Produit non trouvé.");
//            return response;
//        }
//        Product product = productOptional.get();
//
//        // Vérifier la quantité disponible en stock
//        if (quantity > product.getQte_en_stock()) {
//            response.put("status", "error");
//            response.put("message", "Quantité insuffisante en stock.");
//            return response;
//        }
//
//        // Récupérer ou créer un nouveau panier basé sur l'ID de panier (ou autre identifiant)
//        Panier panier = panierDao.findById(panierId).orElse(new Panier());
//
//        // Vérifier si le produit est déjà dans le panier
//        if (panier.getProducts().contains(product)) {
//            response.put("status", "error");
//            response.put("message", "Le produit est déjà dans le panier.");
//            return response;
//        }
//
//        // Ajouter le produit au panier
//        product.setQte_user(quantity);  // Associer la quantité demandée
//        panier.getProducts().add(product);  // Ajouter le produit au panier
//        panier.setQteTotal(panier.getQteTotal() + quantity);  // Mettre à jour la quantité totale dans le panier
//        panier.calculateTotalPrice();  // Calculer le prix total du panier
//
//        // Mettre à jour la quantité en stock du produit
//        product.setQte_en_stock(product.getQte_en_stock() - quantity);
//        productDao.save(product);  // Sauvegarder les modifications du produit
//
//        // Sauvegarder le panier
//        panierDao.save(panier);
//
//        response.put("status", "succès");
//        response.put("message", "Produit ajouté au panier avec succès.");
//
//        return response;
//    }

    @Override
    public HashMap<String, String> addProductToPanier(int productId, int quantity) {
        HashMap<String, String> response = new HashMap<>();
        Optional<Product> productOptional = productDao.findById(productId);

        // Trouver le panier actif
        List<Panier> activePanierList = panierDao.findAllActivePanier();
        Panier panier;

        if (activePanierList.isEmpty()) {
            // Aucun panier actif trouvé, créer un nouveau panier
            panier = new Panier();
            panier.setIsActive(true); // Mettre le panier en actif
            panierDao.save(panier); // Sauvegarder le nouveau panier dans la base de données
        } else {
            panier = activePanierList.get(0); // Utiliser le premier panier actif trouvé
        }
        if (productOptional.isPresent()) {
            Product product = productOptional.get();

            if (quantity <= product.getQte_en_stock()) {
                product.setQte_user(quantity);

                if (!panier.getProducts().contains(product)) {
                    panier.getProducts().add(product);
                    panier.setQteTotal(panier.getQteTotal() + quantity);
                    panier.calculateTotalPrice();

                    product.setQte_en_stock(product.getQte_en_stock() - quantity);
                    productDao.save(product);
                    panierDao.save(panier);

                    response.put("status", "success");
                    response.put("message", "Product added to cart successfully.");
                } else {
                    response.put("status", "error");
                    response.put("message", "Product already exists in the cart.");
                }
            } else {
                response.put("status", "error");
                response.put("message", "Insufficient quantity available.");
            }
        } else {
            response.put("status", "error");
            response.put("message", "Product not found.");
        }

        return response;
    }
    @Override
    public Panier getPanierActive() {
        return panierDao.findByIsActiveTrue()
                .orElseThrow(() -> new IllegalArgumentException("Active Panier not found"));
    }

    @Transactional
    public HashMap<String, String> removeProductFromPanier(int productId) {
        HashMap response = new HashMap<>();
        Optional<Product> productOptional = productDao.findById(productId);

        // Trouver ou créer un panier actif
        Panier panier = panierDao.findByIsActiveTrue().orElse(null);

        if (panier == null) {
            response.put("status", "Error");
            response.put("mesaage", "Cart not found");
            return response;
        }
        // Log products in cart for debugging
        System.out.println("Produits dans le panier: " + panier.getProducts());

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            //verifier si le product et dans la carte
            if (panier.getProducts().stream().anyMatch(p -> p.getId() == productId)) {
                int quantityInCart = product.getQte_user();
                panier.getProducts().remove(product);
                panier.setQteTotal(panier.getQteTotal() - quantityInCart);
                product.setQte_en_stock(product.getQte_en_stock() + quantityInCart);
                productDao.save(product);
                //Verifier si la carte est maintenant vide
                if (panier.getProducts().isEmpty()) {
                    //supprimer la carte si elle est vide
                    panierDao.delete(panier);
                    Panier nouveauPanier = new Panier();
                    nouveauPanier.setIsActive(true);  // Activer le nouveau panier
                    panierDao.save(nouveauPanier);
                    response.put("status", "Success");
                    response.put("mesaage", "product removed and cart deleted as it was empty");
                    return response;
                } else {
                    //recalculer le prix total de la carte
                    panier.calculateTotalPrice();
                    panierDao.save(panier);
                    response.put("status", "Success");
                    response.put("mesaage", "product removed from cart successfully");
                }
            } else {
                response.put("status", "error");
                response.put("message", "Product not found in cart");
            }
        } else {
            response.put("status", "error");
            response.put("message", "Product not found ");
        }
        return response;
    }

        }




