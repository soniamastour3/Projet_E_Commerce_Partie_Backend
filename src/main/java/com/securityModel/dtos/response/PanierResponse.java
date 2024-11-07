package com.securityModel.dtos.response;

import com.securityModel.modele.Panier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PanierResponse {
    private Long id;
    private double totalPrice;
    private boolean isActive;
    private List<ProductToAdd> products; // Liste des produits à ajouter dans le panier

    // Méthode statique pour créer un PanierResponse à partir d'un objet Panier
    public static PanierResponse fromPanier(Panier panier) {
        PanierResponse response = new PanierResponse();
        response.setId(panier.getId());
        response.setTotalPrice(panier.getPricetotaltotal());
        response.setActive(panier.isActive());

        // Convertir les produits de Panier en ProductToAdd
        List<ProductToAdd> productList = panier.getProducts().stream()
                .map(product -> new ProductToAdd(
                        (long) product.getId(),
                        product.getQte_user(),
                        product.getName(),
                        product.getRet(),
                        product.getColor(),
                        product.getQte(),
                        product.getQte_en_stock(),
                        product.getPrice(),
                        product.getDescription(),
                        product.getNbreOrder(),
                        product.getImage()))
                .toList();

        response.setProducts(productList);
        return response;
    }




    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductToAdd {
        private Long productId;
        private int quantity;
        private String name;
        private String ret;
        private String color;
        private Integer qte;
        private Integer qte_en_stock;
        private Double price;
        private String description;
        private int nbreOrder = 0;
        private String image;
    }

}