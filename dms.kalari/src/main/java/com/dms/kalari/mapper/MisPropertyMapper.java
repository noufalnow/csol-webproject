package com.dms.kalari.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.dms.kalari.common.BaseMapper;
import com.dms.kalari.dto.PropertyDTO;
import com.dms.kalari.entity.MisProperty;

@Mapper(componentModel = "spring")
public interface MisPropertyMapper extends BaseMapper<MisProperty, PropertyDTO> {
    MisPropertyMapper INSTANCE = Mappers.getMapper(MisPropertyMapper.class);

    // Custom mappings for specific fields can be added here if necessary
}
