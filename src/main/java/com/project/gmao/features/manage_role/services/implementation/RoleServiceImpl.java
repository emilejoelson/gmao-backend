package com.project.gmao.features.manage_role.services.implementation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.project.gmao.common.constants.Constants;
import com.project.gmao.common.enums.PermissionEnum;
import com.project.gmao.common.enums.RoleEnum;
import com.project.gmao.core.exception.ElementAlreadyExistsException;
import com.project.gmao.core.exception.ElementNotFoundException;
import com.project.gmao.features.manage_permission.entity.Permission;
import com.project.gmao.features.manage_permission.repository.PermissionRepository;
import com.project.gmao.features.manage_role.dto.request.AssignPermissionsToRoleRequest;
import com.project.gmao.features.manage_role.dto.request.RoleCreateRequest;
import com.project.gmao.features.manage_role.dto.request.RolePermissionAssignmentRequest;
import com.project.gmao.features.manage_role.dto.request.RoleRequest;
import com.project.gmao.features.manage_role.dto.request.RoleUpdateRequest;
import com.project.gmao.features.manage_role.dto.response.RoleResponse;
import com.project.gmao.features.manage_role.entity.Role;
import com.project.gmao.features.manage_role.mapper.RoleMapper;
import com.project.gmao.features.manage_role.repository.RoleRepository;
import com.project.gmao.features.manage_role.services.interfaces.RoleService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper = RoleMapper.INSTANCE;

    @Override
    public RoleResponse createRole(RoleRequest request) {
        if (roleRepository.existsByName(request.name())) {
            throw new ElementAlreadyExistsException("Role already exists");
        }

        Role role = roleMapper.roleRequestToRole(request);

        if (request.permissions() != null && !request.permissions().isEmpty()) {
            Set<Permission> permissions = permissionRepository.findByNameIn(request.permissions())
                    .stream().collect(Collectors.toSet());
            role.setPermissions(permissions);
        }

        Role savedRole = roleRepository.save(role);
        return roleMapper.roleToRoleResponse(savedRole);
    }

    @Override
    @Transactional
    public RoleResponse getRoleById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Role not found"));
        return roleMapper.roleToRoleResponse(role);
    }

    @Override
    @Transactional
    public RoleResponse getRoleByUuid(UUID uuid) {
        Role role = roleRepository.findByUuid(uuid)
                .orElseThrow(() -> new ElementNotFoundException("Role not found"));
        return roleMapper.roleToRoleResponse(role);
    }

    @Override
    @Transactional
    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(roleMapper::roleToRoleResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RoleResponse updateRole(UUID uuid, RoleRequest request) {
        Role role = roleRepository.findByUuid(uuid)
                .orElseThrow(() -> new ElementNotFoundException("Role not found"));

        if (!role.getName().equals(request.name()) && 
            roleRepository.existsByName(request.name())) {
            throw new ElementAlreadyExistsException("Role already exists");
        }

        role.setName(request.name());
        role.setDescription(request.description());

        if (request.permissions() != null) {
            Set<Permission> permissions = permissionRepository.findByNameIn(request.permissions())
                    .stream().collect(Collectors.toSet());
            role.setPermissions(permissions);
        }

        Role updatedRole = roleRepository.save(role);
        return roleMapper.roleToRoleResponse(updatedRole);
    }

    @Override
    public void deleteRole(UUID uuid) {
        Role role = roleRepository.findByUuid(uuid)
                .orElseThrow(() -> new ElementNotFoundException("Role not found"));
        roleRepository.delete(role);
    }

    @Override
    public RoleResponse assignPermissionsToRole(RolePermissionAssignmentRequest request) {
        Role role = roleRepository.findByUuid(request.roleUuid())
                .orElseThrow(() -> new ElementNotFoundException("Role not found"));

        Set<Permission> permissions = permissionRepository.findByNameIn(request.permissions())
                .stream().collect(Collectors.toSet());

        role.getPermissions().addAll(permissions);
        Role updatedRole = roleRepository.save(role);
        return roleMapper.roleToRoleResponse(updatedRole);
    }

    @Override
    public RoleResponse removePermissionsFromRole(UUID roleUuid, Set<PermissionEnum> permissions) {
        Role role = roleRepository.findByUuid(roleUuid)
                .orElseThrow(() -> new ElementNotFoundException("Role not found"));

        Set<Permission> permissionsToRemove = permissionRepository.findByNameIn(permissions)
                .stream().collect(Collectors.toSet());

        role.getPermissions().removeAll(permissionsToRemove);
        Role updatedRole = roleRepository.save(role);
        return roleMapper.roleToRoleResponse(updatedRole);
    }

    @Override
    public Role findByName(RoleEnum name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new ElementNotFoundException("Role not found"));
    }
}