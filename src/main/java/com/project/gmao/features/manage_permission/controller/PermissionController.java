package com.project.gmao.features.manage_permission.controller;

import com.project.gmao.common.constants.Constants;
import com.project.gmao.features.manage_permission.entity.Permission;
import com.project.gmao.shared.services.interfaces.EntityNameService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.gmao.features.manage_permission.dto.request.PermissionRequest;
import com.project.gmao.features.manage_permission.dto.response.PermissionResponse;
import com.project.gmao.features.manage_permission.services.interfaces.PermissionService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;
    private final EntityNameService entityNameService;
    private final MessageSource messageSource;

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE')")
    public ResponseEntity<PermissionResponse> createPermission(@Valid @RequestBody PermissionRequest request) {
        PermissionResponse permission = permissionService.createPermission(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(permission);
    }

    @GetMapping("/{uuid}")
    @PreAuthorize("hasAuthority('VIEW')")
    public ResponseEntity<PermissionResponse> getPermission(@PathVariable UUID uuid) {
        PermissionResponse permission = permissionService.getPermissionByUuid(uuid);
        return ResponseEntity.status(HttpStatus.OK).body(permission);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('VIEW')")
    public ResponseEntity<List<PermissionResponse>> getAllPermissions() {
        List<PermissionResponse> permissions = permissionService.getAllPermissions();
        return ResponseEntity.status(HttpStatus.OK).body(permissions);
    }

    @PutMapping("/{uuid}")
    @PreAuthorize("hasAuthority('UPDATE')")
    public ResponseEntity<PermissionResponse> updatePermission(@PathVariable UUID uuid, @Valid @RequestBody PermissionRequest request) {
        PermissionResponse permission = permissionService.updatePermission(uuid, request);
        return ResponseEntity.status(HttpStatus.OK).body(permission);
    }

    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasAuthority('DELETE')")
    public ResponseEntity<String> deletePermission(@PathVariable UUID uuid) {
        permissionService.deletePermission(uuid);
        String entityName = entityNameService.getEntityName(Permission.class);
        String deletionMessage = messageSource.getMessage(Constants.ENTITY_DELETED,new Object[]{entityName}, LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.OK).body(deletionMessage);
    }
}
