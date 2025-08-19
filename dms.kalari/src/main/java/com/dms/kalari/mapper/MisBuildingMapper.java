package com.dms.kalari.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.dms.kalari.common.BaseMapper;
import com.dms.kalari.dto.BuildingDTO;
import com.dms.kalari.entity.MisBuilding;

@Mapper(componentModel = "spring")
public interface MisBuildingMapper extends BaseMapper<MisBuilding, BuildingDTO> {
    MisBuildingMapper INSTANCE = Mappers.getMapper(MisBuildingMapper.class);

    // Custom mappings for specific fields can be added here if necessary
}
