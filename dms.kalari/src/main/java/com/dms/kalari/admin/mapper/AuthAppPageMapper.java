package com.dms.kalari.admin.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.dms.kalari.admin.dto.AuthAppPageDTO;
import com.dms.kalari.admin.entity.AuthAppPage;
import com.dms.kalari.common.BaseMapper;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AuthAppPageMapper extends BaseMapper<AuthAppPage, AuthAppPageDTO> {
    AuthAppPageMapper INSTANCE = Mappers.getMapper(AuthAppPageMapper.class);
}
