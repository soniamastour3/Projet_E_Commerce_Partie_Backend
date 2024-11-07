package com.securityModel.repository;


import com.securityModel.modele.Customer;
import com.securityModel.modele.Panier;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PanierDao extends JpaRepository<Panier, Long> {
    Optional<Panier> findById(Long id);

    Optional<Panier> findByIsActiveTrue();

    @Query("SELECT p FROM Panier p WHERE p.isActive = true")
    List<Panier> findAllActivePanier();



    @Query("SELECT p FROM Panier p WHERE p.isActive = true")
    Optional<Panier> findActivePanier();

    @Modifying
    @Transactional
    @Query("UPDATE Panier p SET p.isActive = false WHERE p.id = :panierId")
    void deactivatePanierById(@Param("panierId") Long panierId);
}
