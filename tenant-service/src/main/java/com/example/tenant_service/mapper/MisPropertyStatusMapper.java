package com.example.tenant_service.mapper;

import com.example.tenant_service.common.BaseMapper;
import com.example.tenant_service.dto.PropertyStatusDTO;
import com.example.tenant_service.entity.MisPropertyStatus;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MisPropertyStatusMapper extends BaseMapper<MisPropertyStatus, PropertyStatusDTO> {
    MisPropertyStatusMapper INSTANCE = Mappers.getMapper(MisPropertyStatusMapper.class);

    // Custom mappings for specific fields can be added here if necessary
}
