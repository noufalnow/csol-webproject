package com.example.tenant_service.mapper;

import com.example.tenant_service.common.BaseMapper;
import com.example.tenant_service.dto.PropertyDTO;
import com.example.tenant_service.entity.MisProperty;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MisPropertyMapper extends BaseMapper<MisProperty, PropertyDTO> {
    MisPropertyMapper INSTANCE = Mappers.getMapper(MisPropertyMapper.class);

    // Custom mappings for specific fields can be added here if necessary
}
