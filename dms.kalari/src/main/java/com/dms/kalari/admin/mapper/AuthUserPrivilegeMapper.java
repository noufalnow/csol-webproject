package com.dms.kalari.admin.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.dms.kalari.admin.dto.AuthUserPrivilegeDTO;
import com.dms.kalari.admin.entity.AuthUserPrivilege;
import com.dms.kalari.common.BaseMapper;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AuthUserPrivilegeMapper extends BaseMapper<AuthUserPrivilege, AuthUserPrivilegeDTO> {
    AuthUserPrivilegeMapper INSTANCE = Mappers.getMapper(AuthUserPrivilegeMapper.class);
}