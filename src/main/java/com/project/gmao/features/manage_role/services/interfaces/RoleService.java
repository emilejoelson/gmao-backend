package com.project.gmao.features.manage_role.services.interfaces;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.project.gmao.common.enums.PermissionEnum;
import com.project.gmao.common.enums.RoleEnum;
import com.project.gmao.features.manage_role.dto.request.RolePermissionAssignmentRequest;
import com.project.gmao.features.manage_role.dto.request.RoleRequest;
import com.project.gmao.features.manage_role.dto.response.RoleResponse;
import com.project.gmao.features.manage_role.entity.Role;

public interface RoleService {
    RoleResponse createRole(RoleRequest request);
    RoleResponse getRoleById(Long id);
    RoleResponse getRoleByUuid(UUID uuid);
    List<RoleResponse> getAllRoles();
    RoleResponse updateRole(UUID uuid, RoleRequest request);
    void deleteRole(UUID uuid);
    RoleResponse assignPermissionsToRole(RolePermissionAssignmentRequest request);
    RoleResponse removePermissionsFromRole(UUID roleUuid, Set<String> permissions);
    Role findByName(String name);
}

