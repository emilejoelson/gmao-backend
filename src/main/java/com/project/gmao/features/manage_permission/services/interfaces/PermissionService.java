package com.project.gmao.features.manage_permission.services.interfaces;

import java.util.List;
import java.util.UUID;

import com.project.gmao.common.enums.PermissionEnum;
import com.project.gmao.features.manage_permission.dto.request.PermissionRequest;
import com.project.gmao.features.manage_permission.dto.response.PermissionResponse;
import com.project.gmao.features.manage_permission.entity.Permission;

public interface PermissionService {
    PermissionResponse createPermission(PermissionRequest request);
    PermissionResponse getPermissionById(Long id);
    PermissionResponse getPermissionByUuid(UUID uuid);
    List<PermissionResponse> getAllPermissions();
    PermissionResponse updatePermission(UUID uuid, PermissionRequest request);
    void deletePermission(UUID uuid);
    Permission findByName(PermissionEnum name);
}