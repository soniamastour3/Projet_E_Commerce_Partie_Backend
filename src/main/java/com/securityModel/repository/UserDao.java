package com.securityModel.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.securityModel.modele.User;

@Repository
public interface UserDao extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);

  User findByEmail(String email);

  User findByPasswordResetToken(String passwordResetToken);


}
