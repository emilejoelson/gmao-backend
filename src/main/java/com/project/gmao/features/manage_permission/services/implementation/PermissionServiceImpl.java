package com.project.gmao.features.manage_permission.services.implementation;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.project.gmao.common.constants.Constants;
import com.project.gmao.common.enums.PermissionEnum;
import com.project.gmao.core.exception.ElementAlreadyExistsException;
import com.project.gmao.core.exception.ElementNotFoundException;
import com.project.gmao.features.manage_permission.dto.request.PermissionCreateRequest;
import com.project.gmao.features.manage_permission.dto.request.PermissionRequest;
import com.project.gmao.features.manage_permission.dto.request.PermissionUpdateRequest;
import com.project.gmao.features.manage_permission.dto.response.PermissionResponse;
import com.project.gmao.features.manage_permission.entity.Permission;
import com.project.gmao.features.manage_permission.mapper.PermissionMapper;
import com.project.gmao.features.manage_permission.repository.PermissionRepository;
import com.project.gmao.features.manage_permission.services.interfaces.PermissionService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper = PermissionMapper.INSTANCE;

    @Override
    public PermissionResponse createPermission(PermissionRequest request) {
        if (permissionRepository.existsByName(request.name())) {
            throw new ElementAlreadyExistsException("Permission already exists");
        }

        Permission permission = permissionMapper.permissionRequestToPermission(request);
        Permission savedPermission = permissionRepository.save(permission);
        return permissionMapper.permissionToPermissionResponse(savedPermission);
    }

    @Override
    @Transactional
    public PermissionResponse getPermissionById(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Permission not found"));
        return permissionMapper.permissionToPermissionResponse(permission);
    }

    @Override
    @Transactional
    public PermissionResponse getPermissionByUuid(UUID uuid) {
        Permission permission = permissionRepository.findByUuid(uuid)
                .orElseThrow(() -> new ElementNotFoundException("Permission not found"));
        return permissionMapper.permissionToPermissionResponse(permission);
    }

    @Override
    @Transactional
    public List<PermissionResponse> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(permissionMapper::permissionToPermissionResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PermissionResponse updatePermission(UUID uuid, PermissionRequest request) {
        Permission permission = permissionRepository.findByUuid(uuid)
                .orElseThrow(() -> new ElementNotFoundException("Permission not found"));

        if (!permission.getName().equals(request.name()) && 
            permissionRepository.existsByName(request.name())) {
            throw new ElementAlreadyExistsException("Permission already exists");
        }

        permission.setName(request.name());
        permission.setDescription(request.description());

        Permission updatedPermission = permissionRepository.save(permission);
        return permissionMapper.permissionToPermissionResponse(updatedPermission);
    }

    @Override
    public void deletePermission(UUID uuid) {
        Permission permission = permissionRepository.findByUuid(uuid)
                .orElseThrow(() -> new ElementNotFoundException("Permission not found"));
        permissionRepository.delete(permission);
    }

    @Override
    public Permission findByName(PermissionEnum name) {
        return permissionRepository.findByName(name)
                .orElseThrow(() -> new ElementNotFoundException("Permission not found"));
    }
}