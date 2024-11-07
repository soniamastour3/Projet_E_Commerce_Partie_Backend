package com.securityModel.repository;


import com.securityModel.modele.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishListDao extends JpaRepository<WishList, Long> {
    Optional<WishList> findByUserId(Long userId);
}
