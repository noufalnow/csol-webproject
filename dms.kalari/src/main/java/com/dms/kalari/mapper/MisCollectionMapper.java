package com.dms.kalari.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.dms.kalari.common.BaseMapper;
import com.dms.kalari.dto.MisCollectionDTO;
import com.dms.kalari.entity.MisCollection;

@Mapper(componentModel = "spring")
public interface MisCollectionMapper extends BaseMapper<MisCollection, MisCollectionDTO> {
    MisCollectionMapper INSTANCE = Mappers.getMapper(MisCollectionMapper.class);
    
    
    MisCollectionDTO toDTO(MisCollection misCollection);

    // Map MisCollectionDTO to MisCollection
    MisCollection toEntity(MisCollectionDTO misCollectionDTO);
}
