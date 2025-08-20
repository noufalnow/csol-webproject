package com.dms.kalari.admin.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.dms.kalari.admin.dto.AuthAppMenuDTO;
import com.dms.kalari.admin.entity.AuthAppMenu;
import com.dms.kalari.common.BaseMapper;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AuthAppMenuMapper extends BaseMapper<AuthAppMenu, AuthAppMenuDTO> {
    AuthAppMenuMapper INSTANCE = Mappers.getMapper(AuthAppMenuMapper.class);
}