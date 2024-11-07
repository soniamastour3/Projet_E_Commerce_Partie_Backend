package com.securityModel.controllers;


import com.securityModel.Services.OrderService;
import com.securityModel.dtos.request.OrderRequest;
import com.securityModel.dtos.response.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;


@RestController
@RequestMapping("/orders")
public class OrderController {
    public final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{customer_id}")
    public ResponseEntity<List<OrderResponse>> getOrdersByIdClient(@PathVariable Long customer_id) {
        List<OrderResponse> orders = orderService.getOrdersByIdClient(customer_id);
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/save")
    public OrderResponse addOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.createOrder(orderRequest);
    }

    @GetMapping("/all")
    public List<OrderResponse> AllOrder() {
        return orderService.allOrder();
    }

    @GetMapping("/getone/{id}")
    public OrderResponse orderbyid(@PathVariable Long id) {
        return orderService.orderById(id);
    }

    @PutMapping("/update/{id}")
    public OrderResponse updateorder(@RequestBody OrderRequest orderRequest, @PathVariable Long id) {
        return orderService.updateorder(orderRequest, id);
    }

    @DeleteMapping("/delete/{id}")
    public HashMap<String, String> deleteorder(@PathVariable Long id) {
        return orderService.deleteorder(id);
    }

    @PostMapping(value = {"/saveOrder/{clientId}", "/saveOrder/{clientId}/{productIds}"})
    public OrderResponse saveOrder(@RequestBody OrderRequest orderRequest, @PathVariable("clientId") Long clientId, @PathVariable(value = "productIds", required = false) List<Integer> productIds) {
        return orderService.saveOrder(orderRequest, clientId, productIds);
    }
    @PostMapping("/passerOrder/{customer_id}")
    public ResponseEntity<OrderResponse> passerOrder(@RequestBody OrderRequest orderRequest, @PathVariable Long customer_id) {
        try {
            // Appeler le service pour passer la commande
            OrderResponse orderResponse = orderService.passerOrder(orderRequest, customer_id);
            return ResponseEntity.ok(orderResponse);
        } catch (IllegalArgumentException e) {
            // Retourner une réponse avec un statut d'erreur en cas d'exception
            return ResponseEntity.badRequest().body(null);
        }
    }
}
