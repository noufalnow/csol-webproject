package com.example.tenant_service.mapper;

import com.example.tenant_service.common.BaseMapper;
import com.example.tenant_service.dto.PropertyPayOptionDTO;
import com.example.tenant_service.entity.MisPropertyPayOption;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MisPropertyPayOptionMapper extends BaseMapper<MisPropertyPayOption, PropertyPayOptionDTO> {
    MisPropertyPayOptionMapper INSTANCE = Mappers.getMapper(MisPropertyPayOptionMapper.class);
}
