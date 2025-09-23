package com.project.gmao.features.manage_permission.repository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.gmao.common.enums.PermissionEnum;
import com.project.gmao.features.manage_permission.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByName(PermissionEnum name);
    Optional<Permission> findByUuid(UUID uuid);
    List<Permission> findByNameIn(Set<PermissionEnum> names);
    boolean existsByName(PermissionEnum name);
}
