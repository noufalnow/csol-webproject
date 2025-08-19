package com.dms.kalari.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.dms.kalari.common.BaseMapper;
import com.dms.kalari.dto.PropertyStatusDTO;
import com.dms.kalari.entity.MisPropertyStatus;

@Mapper(componentModel = "spring")
public interface MisPropertyStatusMapper extends BaseMapper<MisPropertyStatus, PropertyStatusDTO> {
    MisPropertyStatusMapper INSTANCE = Mappers.getMapper(MisPropertyStatusMapper.class);

    // Custom mappings for specific fields can be added here if necessary
}
