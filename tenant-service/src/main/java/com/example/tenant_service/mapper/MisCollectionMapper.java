package com.example.tenant_service.mapper;

import com.example.tenant_service.common.BaseMapper;
import com.example.tenant_service.dto.MisCollectionDTO;
import com.example.tenant_service.entity.MisCollection;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MisCollectionMapper extends BaseMapper<MisCollection, MisCollectionDTO> {
    MisCollectionMapper INSTANCE = Mappers.getMapper(MisCollectionMapper.class);
    
    
    MisCollectionDTO toDTO(MisCollection misCollection);

    // Map MisCollectionDTO to MisCollection
    MisCollection toEntity(MisCollectionDTO misCollectionDTO);
}
