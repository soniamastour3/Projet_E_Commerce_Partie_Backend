package com.securityModel.Services;


import com.securityModel.dtos.request.OrderRequest;
import com.securityModel.dtos.response.OrderResponse;

import java.util.HashMap;
import java.util.List;
public interface OrderService {
    List<OrderResponse> getOrdersByIdClient(Long customer_id);
    OrderResponse createOrder(OrderRequest order);
    List<OrderResponse> allOrder();
    OrderResponse orderById(Long id);
    OrderResponse updateorder(OrderRequest orderRequest, Long id);
    HashMap<String, String> deleteorder(Long id);
    OrderResponse saveOrder(OrderRequest orderRequest, Long id_client, List<Integer> ids);
    OrderResponse passerOrder(OrderRequest orderRequest, Long customer_id);


}
