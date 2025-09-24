package com.project.gmao.features.manage_role.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.gmao.common.enums.RoleEnum;
import com.project.gmao.features.manage_role.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
    Optional<Role> findByUuid(UUID uuid);
    List<Role> findByNameIn(Set<String> names);
    boolean existsByName(String name);
}

