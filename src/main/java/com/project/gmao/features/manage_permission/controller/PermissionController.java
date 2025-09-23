package com.project.gmao.features.manage_permission.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.gmao.features.manage_permission.dto.request.PermissionCreateRequest;
import com.project.gmao.features.manage_permission.dto.request.PermissionRequest;
import com.project.gmao.features.manage_permission.dto.request.PermissionUpdateRequest;
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
        return ResponseEntity.ok(permission);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('VIEW')")
    public ResponseEntity<List<PermissionResponse>> getAllPermissions() {
        List<PermissionResponse> permissions = permissionService.getAllPermissions();
        return ResponseEntity.ok(permissions);
    }

    @PutMapping("/{uuid}")
    @PreAuthorize("hasAuthority('UPDATE')")
    public ResponseEntity<PermissionResponse> updatePermission(@PathVariable UUID uuid, @Valid @RequestBody PermissionRequest request) {
        PermissionResponse permission = permissionService.updatePermission(uuid, request);
        return ResponseEntity.ok(permission);
    }

    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasAuthority('DELETE')")
    public ResponseEntity<Void> deletePermission(@PathVariable UUID uuid) {
        permissionService.deletePermission(uuid);
        return ResponseEntity.noContent().build();
    }
}
