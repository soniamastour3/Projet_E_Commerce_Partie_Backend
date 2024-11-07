package com.securityModel.Services.impl;


import com.securityModel.Services.OrderService;
import com.securityModel.dtos.request.OrderRequest;
import com.securityModel.dtos.response.OrderResponse;
import com.securityModel.modele.*;
import com.securityModel.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceIMPL implements OrderService {

    private final OrderDao orderDaoconst;
    private final ProductDao productDao;
    private final ClientDao clientDao;
    private final PanierDao panierDao;
    private final OrderDao orderDao;

    public OrderServiceIMPL(OrderDao orderDaoconst, ProductDao productDao, ClientDao clientDao,  PanierDao panierDao, OrderDao orderDao) {
        this.orderDaoconst= orderDaoconst;
        this.productDao = productDao;
        this.clientDao = clientDao;
        this.panierDao = panierDao;
        this.orderDao = orderDao;
    }

    @Override
    public List<OrderResponse> getOrdersByIdClient(Long customer_id) {
        // Retrieve the client by ID
        Optional<Customer> clientOpt = clientDao.findById(customer_id);

        if (!clientOpt.isPresent()) {
            throw new RuntimeException("Client not found with id: " + customer_id);
        }

        // Get all orders associated with the client
        Customer customer = clientOpt.get();
        Collection<Order> orders = customer.getOrders();

        // Convert the orders to OrderResponse DTOs and return the list
        return orders.stream()
                .map(order -> OrderResponse.fromEntity(order))
                .collect(Collectors.toList());
    }


    public OrderResponse createOrder(OrderRequest orderRequest) {
        // Création de l'entité Order à partir du DTO
        Order order = OrderResponse.toEntity(orderRequest);

        // Récupération et association du client
        if (orderRequest.getCustomer() != null) {
            Customer customer = clientDao.findById(orderRequest.getCustomer().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Client non trouvé avec l'ID : " + orderRequest.getCustomer()));
            order.setCustomer(customer);
        }

        // Récupération et association des produits
        if (orderRequest.getProductIds() != null && !orderRequest.getProductIds().isEmpty()) {
            List<Product> products = orderRequest.getProductIds().stream()
                    .map(productId -> productDao.findById(productId)
                            .orElseThrow(() -> new IllegalArgumentException("Produit non trouvé avec l'ID : " + productId)))
                    .collect(Collectors.toList());
            order.setProducts(products);
        }

        // Sauvegarde de la commande
        Order savedOrder = orderDaoconst.save(order);

        // Retour du DTO de réponse
        return OrderResponse.fromEntity(savedOrder);
    }

    @Override
    public List<OrderResponse> allOrder(){
        return orderDaoconst.findAll().stream()
                .map(OrderResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse orderById(Long id) {
        return orderDaoconst.findById(id)
                .map(OrderResponse::fromEntity)
                .orElseThrow(() ->new RuntimeException("Order not found with this id:" +id));
    }

    @Override
    public OrderResponse updateorder(OrderRequest orderRequest, Long id) {
        Order order=orderDaoconst.findById(id).orElseThrow(()->
                new RuntimeException("Order not found with this id:" +id));
        if (order != null) {
            Order o = OrderResponse.toEntity(orderRequest);
            o.setId(id);
            o.setDescription(o.getDescription() == null ? order.getDescription() : o.getDescription());
            Order savedorder = orderDaoconst.save(o);
            return OrderResponse.fromEntity(savedorder);
        }else{
            return null;
        }
    }

    @Override
    public HashMap<String, String> deleteorder(Long id) {
        HashMap message =new HashMap<>();
        Order o = orderDaoconst.findById(id).orElse(null);
        if (o != null) {
            try {
                orderDaoconst.deleteById(id);
                message.put("message", "Order deleted successfully");
            }catch (Exception a){
                message.put("message", a.getMessage());
            }
        }else{
            message.put("message", "Order not found: " +id);
        }
        return message;
    }



    @Override
    public OrderResponse saveOrder(OrderRequest orderRequest, Long clientId, List<Integer> productIds) {
        List<String> messages = new ArrayList<>();

        for (int productId : productIds) {
            Product product = productDao.findById(productId).orElse(null);

            if (product != null) {
                // Update product and add it to the order
                product.setNbreOrder(product.getNbreOrder() + 1);
                product.setQte_en_stock(product.getQte_en_stock() - 1); // Decrement stock quantity

                // Check if the quantity is low and add a warning message
                if (product.getQte_en_stock() < 20) {
                    String message = "Le produit " + product.getName()
                            + " a une quantité faible dans le stock ! Il en reste seulement "
                            + product.getQte_en_stock();
                    messages.add(message);
                }

                orderRequest.addproduct(product);
            }
        }

        Customer customer = clientDao.findById(clientId).orElseThrow(()->

            new IllegalArgumentException("Client not found with ID: " + clientId));


        Order order = OrderResponse.toEntity(orderRequest);
        order.setCustomer(customer);
        order.setMessages(messages);

        Order savedOrder = orderDaoconst.save(order);
        return OrderResponse.fromEntity(savedOrder);
    }
    @Transactional
    @Override
    public OrderResponse passerOrder(OrderRequest orderRequest, Long customer_id) {
        // Find the customer
        Customer customer = clientDao.findById(customer_id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        // Find the active panier
        Panier activePanier = panierDao.findActivePanier()
                .orElseThrow(() -> new RuntimeException("Aucun panier actif trouvé"));

        // Check if an order already exists for this active panier
        if (orderDao.existsByPanierId(activePanier.getId())) {
            throw new IllegalArgumentException("Le panier a déjà une commande associée.");
        }

        // Create a new order
        Order order = new Order();
        order.setCustomer(customer);
        order.setPanier(activePanier);  // Associate the order with the active panier

        // Mettre à jour les quantités des produits en stock
        List<Product> products = activePanier.getProducts();
        for (Product product : products) {
            int quantityInStock = product.getQte_en_stock();
            int quantityOrdered = product.getQte_user();

            if (quantityInStock < quantityOrdered) {
                throw new IllegalArgumentException("Quantité insuffisante pour le produit : " + product.getName());
            }

            // Diminuer la quantité en stock
            product.setQte_en_stock(quantityInStock - quantityOrdered);
            productDao.save(product); // Enregistrer les modifications
        }

        // Compléter la commande avec les informations restantes
        order.setPrice_total(activePanier.getPricetotaltotal());
        order.setQte_total(activePanier.getQteTotal());

        // Save the order
        Order savedOrder = orderDao.save(order);

        // Deactivate the old active panier
        panierDao.deactivatePanierById(activePanier.getId());

        // Create a new empty panier
        Panier newPanier = new Panier();
        newPanier.setIsActive(true);

        // Debugging output to verify state before saving
        System.out.println("Saving new empty panier with active status: " + newPanier.isActive());

        panierDao.save(newPanier);  // Save the new empty panier

        return OrderResponse.fromEntity(savedOrder);  // Return the response
    }
}
