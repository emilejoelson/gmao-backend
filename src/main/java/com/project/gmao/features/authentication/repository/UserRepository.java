package com.project.gmao.features.authentication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.gmao.features.authentication.entity.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailIgnoreCase(String email);
    Optional<User> findByUuid(UUID uuid);
    boolean existsByEmail(String email);
}

