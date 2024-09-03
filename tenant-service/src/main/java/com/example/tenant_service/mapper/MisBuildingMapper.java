package com.example.tenant_service.mapper;

import com.example.tenant_service.common.BaseMapper;
import com.example.tenant_service.dto.BuildingDTO;
import com.example.tenant_service.entity.MisBuilding;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MisBuildingMapper extends BaseMapper<MisBuilding, BuildingDTO> {
    MisBuildingMapper INSTANCE = Mappers.getMapper(MisBuildingMapper.class);

    // Custom mappings for specific fields can be added here if necessary
}
