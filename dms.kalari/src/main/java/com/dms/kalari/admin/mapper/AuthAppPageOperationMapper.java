package com.dms.kalari.admin.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.dms.kalari.admin.dto.AppPageOperationDTO;
import com.dms.kalari.admin.entity.AuthAppPageOperation;
import com.dms.kalari.common.BaseMapper;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AuthAppPageOperationMapper extends BaseMapper<AuthAppPageOperation, AppPageOperationDTO> {
 AuthAppPageOperationMapper INSTANCE = Mappers.getMapper(AuthAppPageOperationMapper.class);
}
