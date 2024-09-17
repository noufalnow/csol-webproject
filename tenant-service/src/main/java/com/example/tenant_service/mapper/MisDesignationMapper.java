package com.example.tenant_service.mapper;

import com.example.tenant_service.common.BaseMapper;
import com.example.tenant_service.dto.DesignationDTO;
import com.example.tenant_service.dto.PropertyDTO;
import com.example.tenant_service.entity.MisDesignation;
import com.example.tenant_service.entity.MisProperty;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface MisDesignationMapper extends BaseMapper<MisDesignation, DesignationDTO> {
	MisDesignationMapper INSTANCE = Mappers.getMapper(MisDesignationMapper.class);

    // Custom mappings for specific fields can be added here if necessary
}

