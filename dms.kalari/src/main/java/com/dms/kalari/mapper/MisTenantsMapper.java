package com.dms.kalari.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.dms.kalari.common.BaseMapper;
import com.dms.kalari.dto.TenantDTO;
import com.dms.kalari.entity.MisTenants;

@Mapper(componentModel = "spring")
public interface MisTenantsMapper extends BaseMapper<MisTenants, TenantDTO> {
    MisTenantsMapper INSTANCE = Mappers.getMapper(MisTenantsMapper.class);

    // Custom mappings for specific fields can be added here if necessary
}
