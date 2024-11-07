package com.securityModel.dtos.response;


import com.securityModel.dtos.request.OrderRequest;
import com.securityModel.modele.Customer;
import com.securityModel.modele.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private String description;
    private int qte_total;
    private double price_total;
    private boolean state;

    private Customer customer;
    private List<ProductResponse> products;
    private UserResponse user;
    private PanierResponse panier;
    private Long customerId;
    private Long userId;  // Store only the user ID
    private Long panierId;  // Store only the panier ID

    public static OrderResponse fromEntity(Order entity) {
        OrderResponse orderResponse = new OrderResponse();
        BeanUtils.copyProperties(entity, orderResponse);

        // Assuming entity has methods to get IDs for related entities
        if (entity.getCustomer() != null) {
            orderResponse.setCustomerId(entity.getCustomer().getId());
        }
        if (entity.getPanier() != null) {
            orderResponse.setPanierId(entity.getPanier().getId());
        }
        if (entity.getUser() != null) {
            orderResponse.setUserId(entity.getUser().getId());
        }

        return orderResponse;
    }

//    public static OrderResponse fromEntity(Order entity) {
//        OrderResponse orderResponse = new OrderResponse();
//        BeanUtils.copyProperties(entity, orderResponse);
//
//        return orderResponse;
//    }

    public static Order toEntity(OrderRequest orderResponse) {
        Order o = new Order();
        BeanUtils.copyProperties(orderResponse, o);
        return o;
    }
}
