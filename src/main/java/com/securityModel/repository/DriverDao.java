package com.securityModel.repository;


import com.securityModel.modele.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverDao extends JpaRepository<Driver, Long> {
}
