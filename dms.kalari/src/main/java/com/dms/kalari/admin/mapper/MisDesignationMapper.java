package com.dms.kalari.admin.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.dms.kalari.admin.dto.DesignationDTO;
import com.dms.kalari.admin.entity.MisDesignation;
import com.dms.kalari.common.BaseMapper;


@Mapper(componentModel = "spring")
public interface MisDesignationMapper extends BaseMapper<MisDesignation, DesignationDTO> {
	MisDesignationMapper INSTANCE = Mappers.getMapper(MisDesignationMapper.class);

    // Custom mappings for specific fields can be added here if necessary
}

