package com.example.tenant_service.mapper;

import com.example.tenant_service.common.BaseMapper;
import com.example.tenant_service.dto.TenantDTO;
import com.example.tenant_service.entity.MisTenants;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MisTenantsMapper extends BaseMapper<MisTenants, TenantDTO> {
    MisTenantsMapper INSTANCE = Mappers.getMapper(MisTenantsMapper.class);

    // Custom mappings for specific fields can be added here if necessary
}
