package com.example.tenant_service.mapper;

import com.example.tenant_service.common.BaseMapper;
import com.example.tenant_service.dto.CollectionDetDTO;
import com.example.tenant_service.entity.MisCollectionDet;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MisCollectionDetMapper extends BaseMapper<MisCollectionDet, CollectionDetDTO> {
    MisCollectionDetMapper INSTANCE = Mappers.getMapper(MisCollectionDetMapper.class);

    // Additional custom mappings can be defined here if required
}
