package com.project.gmao.features.manage_role.controller;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.project.gmao.common.constants.Constants;
import com.project.gmao.features.authentication.entity.User;
import com.project.gmao.features.manage_role.entity.Role;
import com.project.gmao.shared.services.interfaces.EntityNameService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.gmao.common.enums.PermissionEnum;
import com.project.gmao.features.manage_role.dto.request.RolePermissionAssignmentRequest;
import com.project.gmao.features.manage_role.dto.request.RoleRequest;
import com.project.gmao.features.manage_role.dto.response.RoleResponse;
import com.project.gmao.features.manage_role.services.interfaces.RoleService;

import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;
    private final MessageSource messageSource;
    private final EntityNameService entityNameService;

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE')")
    public ResponseEntity<RoleResponse> createRole(@Valid @RequestBody RoleRequest request) {
        RoleResponse role = roleService.createRole(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(role);
    }

    @GetMapping("/{uuid}")
    @PreAuthorize("hasAuthority('VIEW')")
    public ResponseEntity<RoleResponse> getRole(@PathVariable UUID uuid) {
        RoleResponse role = roleService.getRoleByUuid(uuid);
        return ResponseEntity.status(HttpStatus.OK).body(role);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('VIEW')")
    public ResponseEntity<List<RoleResponse>> getAllRoles() {
        List<RoleResponse> roles = roleService.getAllRoles();
        return ResponseEntity.status(HttpStatus.OK).body(roles);
    }

    @PutMapping("/{uuid}")
    @PreAuthorize("hasAuthority('UPDATE')")
    public ResponseEntity<RoleResponse> updateRole(@PathVariable UUID uuid, @Valid @RequestBody RoleRequest request) {
        RoleResponse role = roleService.updateRole(uuid, request);
        return ResponseEntity.status(HttpStatus.OK).body(role);
    }

    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasAuthority('DELETE')")
    public ResponseEntity<String> deleteRole(@PathVariable UUID uuid) {
        roleService.deleteRole(uuid);
        String entityName = entityNameService.getEntityName(Role.class);
        String deletionMessage = messageSource.getMessage(Constants.ENTITY_DELETED,new Object[]{entityName}, LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.OK).body(deletionMessage);
    }

    @PostMapping("/assign-permissions")
     @PreAuthorize("hasAuthority('UPDATE')")
    public ResponseEntity<RoleResponse> assignPermissionsToRole(@Valid @RequestBody RolePermissionAssignmentRequest request) {
        RoleResponse role = roleService.assignPermissionsToRole(request);
        return ResponseEntity.ok(role);
    }

    @DeleteMapping("/{uuid}/permissions")
    @PreAuthorize("hasAuthority('UPDATE')")
    public ResponseEntity<RoleResponse> removePermissionsFromRole(@PathVariable UUID uuid, @RequestBody Set<String> permissions) {
        RoleResponse role = roleService.removePermissionsFromRole(uuid, permissions);
        return ResponseEntity.ok(role);
    }
}