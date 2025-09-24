package com.project.gmao.features.authentication.controller;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.project.gmao.common.constants.Constants;
import com.project.gmao.features.authentication.entity.User;
import com.project.gmao.shared.services.interfaces.EntityNameService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.gmao.common.enums.RoleEnum;
import com.project.gmao.features.authentication.dto.request.SignupRequest;
import com.project.gmao.features.authentication.dto.request.UserRoleAssignmentRequest;
import com.project.gmao.features.authentication.dto.response.UserResponse;
import com.project.gmao.features.authentication.services.interfaces.UserService;

import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final EntityNameService entityNameService;
    private final MessageSource messageSource;

    @GetMapping("/{uuid}")
    @PreAuthorize("hasAuthority('VIEW')")
    public ResponseEntity<UserResponse> getUser(@PathVariable UUID uuid) {
        UserResponse user = userService.getUserByUuid(uuid);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping
    //@PreAuthorize("hasAuthority('VIEW')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @PutMapping("/{uuid}")
    @PreAuthorize("hasAuthority('UPDATE')")
    public ResponseEntity<UserResponse> updateUser(@PathVariable UUID uuid, @Valid @RequestBody SignupRequest request) {
        UserResponse user = userService.updateUser(uuid, request);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasAuthority('DELETE')")
    public ResponseEntity<String> deleteUser(@PathVariable UUID uuid) {
        userService.deleteUser(uuid);
        String entityName = entityNameService.getEntityName(User.class);
        String deletionMessage = messageSource.getMessage(Constants.ENTITY_DELETED,new Object[]{entityName}, LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.OK).body(deletionMessage);
    }

    @PostMapping("/assign-roles")
    // @PreAuthorize("hasAuthority('UPDATE')")
    public ResponseEntity<UserResponse> assignRolesToUser(@Valid @RequestBody UserRoleAssignmentRequest request) {
        UserResponse user = userService.assignRolesToUser(request);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping("/{uuid}/roles")
    @PreAuthorize("hasAuthority('UPDATE')")
    public ResponseEntity<UserResponse> removeRolesFromUser(@PathVariable UUID uuid, @RequestBody Set<String> roles) {
        UserResponse user = userService.removeRolesFromUser(uuid, roles);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
