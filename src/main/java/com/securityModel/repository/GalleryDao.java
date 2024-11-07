package com.securityModel.repository;


import com.securityModel.modele.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GalleryDao extends JpaRepository<Gallery, Long> {
}
