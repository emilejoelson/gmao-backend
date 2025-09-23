package com.project.gmao.features.manage_role.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.project.gmao.features.authentication.dto.response.BaseDto;
import com.project.gmao.features.manage_role.dto.request.RoleRequest;
import com.project.gmao.features.manage_role.dto.response.RoleResponse;
import com.project.gmao.features.manage_role.entity.Role;


@Mapper
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "uuid", ignore = true),
        @Mapping(target = "enabled", ignore = true),
        @Mapping(target = "permissions", ignore = true)
    })
    Role roleRequestToRole(RoleRequest request);

    @Mappings({
        @Mapping(target = "baseDto", expression = "java(toBaseDto(role))"),
        @Mapping(target = "permissions", source = "permissions")
    })
    RoleResponse roleToRoleResponse(Role role);

    default BaseDto toBaseDto(Role role) {
        return new BaseDto(role.getCreatedAt(), role.getUpdatedAt());
    }
}
