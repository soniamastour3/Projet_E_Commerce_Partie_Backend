package com.securityModel.repository;

import com.securityModel.modele.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductDao extends JpaRepository<Product, Integer> {
    //Rechercher par nom
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:name%")
    List<Product> findByName(@Param("name") String name);

    // Rechercher par prix dans un intervalle
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice")
    List<Product> findByPriceBetween(@Param("minPrice") double minPrice, @Param("maxPrice") double maxPrice);


}
