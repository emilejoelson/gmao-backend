// package com.project.gmao.shared.services.implementation;

// import java.util.HashSet;
// import java.util.Set;
// import java.util.stream.Collectors;

// import org.springframework.stereotype.Component;
// import org.springframework.transaction.annotation.Transactional;

// import com.project.gmao.common.enums.PermissionEnum;
// import com.project.gmao.common.enums.RoleEnum;
// import com.project.gmao.features.manage_permission.entity.Permission;
// import com.project.gmao.features.manage_permission.repository.PermissionRepository;
// import com.project.gmao.features.manage_role.entity.Role;
// import com.project.gmao.features.manage_role.repository.RoleRepository;

// import jakarta.annotation.PostConstruct;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;

// @Component
// @RequiredArgsConstructor
// @Slf4j
// public class DataInitializationService {

//     private final PermissionRepository permissionRepository;
//     private final RoleRepository roleRepository;

//     @PostConstruct
//     @Transactional
//     public void initializeData() {
//         // log.info("---- Starting data initialization ----");
//         initializePermissions();
//         initializeRoles();
//         // log.info("---- Data initialization completed ----");
//     }

//     private void initializePermissions() {
//         for (PermissionEnum permissionEnum : PermissionEnum.values()) {
//             if (!permissionRepository.existsByName(permissionEnum)) {
//                 Permission permission = Permission.builder()
//                         .name(permissionEnum)
//                         .description("Permission for " + permissionEnum.getName())
//                         .enabled(true)
//                         .build();
//                 permissionRepository.save(permission);
//                 log.info("Created permission: {}", permissionEnum);
//             }
//         }
//     }

//     private void initializeRoles() {
//         for (RoleEnum roleEnum : RoleEnum.values()) {
//             if (!roleRepository.existsByName(roleEnum)) {
//                 Set<Permission> permissions = getDefaultPermissionsForRole(roleEnum);

//                 Role role = Role.builder()
//                         .name(roleEnum)
//                         .description("Role " + roleEnum.getAuthority())
//                         .enabled(true)
//                         .permissions(permissions)
//                         .build();

//                 roleRepository.save(role);
//                 log.info("Created role: {} with {} permissions", roleEnum, permissions.size());
//             }
//         }
//     }

//     private Set<Permission> getDefaultPermissionsForRole(RoleEnum roleEnum) {
//         Set<PermissionEnum> permissionEnums = new HashSet<>();

//         switch (roleEnum) {
//             case USER -> permissionEnums.add(PermissionEnum.VIEW);
//             case MODERATOR -> {
//                 permissionEnums.add(PermissionEnum.VIEW);
//                 permissionEnums.add(PermissionEnum.CREATE);
//                 permissionEnums.add(PermissionEnum.UPDATE);
//             }
//             case ADMIN -> {
//                 permissionEnums.add(PermissionEnum.VIEW);
//                 permissionEnums.add(PermissionEnum.CREATE);
//                 permissionEnums.add(PermissionEnum.UPDATE);
//                 permissionEnums.add(PermissionEnum.DELETE);
//             }
//         }

//         return permissionRepository.findByNameIn(permissionEnums)
//                 .stream()
//                 .collect(Collectors.toSet());
//     }
// }
