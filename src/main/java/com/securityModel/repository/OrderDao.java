package com.securityModel.repository;



import com.securityModel.modele.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDao extends JpaRepository<Order, Long> {

    boolean existsByPanierId(Long id_panier);
}
