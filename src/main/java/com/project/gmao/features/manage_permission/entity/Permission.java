package com.project.gmao.features.manage_permission.entity;

import com.project.gmao.common.enums.PermissionEnum;
import com.project.gmao.features.manage_role.entity.Role;
import com.project.gmao.shared.entity.BaseEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "permissions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private UUID uuid;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private PermissionEnum name;

    @Column(length = 255)
    private String description;

    @Builder.Default
    private boolean enabled = true;

    @PrePersist
    private void ensureUuid() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID();
        }
    }
}