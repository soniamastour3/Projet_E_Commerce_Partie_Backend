package com.securityModel.repository;


import com.securityModel.modele.Customer;
import com.securityModel.modele.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientDao extends JpaRepository<Customer, Long> {
    Customer findByEmail(String email);
}
