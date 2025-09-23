package com.project.gmao.features.manage_permission.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.project.gmao.features.authentication.dto.response.BaseDto;
import com.project.gmao.features.manage_permission.dto.request.PermissionCreateRequest;
import com.project.gmao.features.manage_permission.dto.request.PermissionRequest;
import com.project.gmao.features.manage_permission.dto.response.PermissionResponse;
import com.project.gmao.features.manage_permission.entity.Permission;

@Mapper
public interface PermissionMapper {
    PermissionMapper INSTANCE = Mappers.getMapper(PermissionMapper.class);

    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "uuid", ignore = true),
        @Mapping(target = "enabled", ignore = true)
    })
    Permission permissionRequestToPermission(PermissionRequest request);

    @Mapping(target = "baseDto", expression = "java(toBaseDto(permission))")
    PermissionResponse permissionToPermissionResponse(Permission permission);

    default BaseDto toBaseDto(Permission permission) {
        return new BaseDto(permission.getCreatedAt(), permission.getUpdatedAt());
    }
}