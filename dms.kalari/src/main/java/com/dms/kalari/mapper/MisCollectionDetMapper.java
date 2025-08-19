package com.dms.kalari.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.dms.kalari.common.BaseMapper;
import com.dms.kalari.dto.CollectionDetDTO;
import com.dms.kalari.entity.MisCollectionDet;

@Mapper(componentModel = "spring")
public interface MisCollectionDetMapper extends BaseMapper<MisCollectionDet, CollectionDetDTO> {
    MisCollectionDetMapper INSTANCE = Mappers.getMapper(MisCollectionDetMapper.class);

    // Additional custom mappings can be defined here if required
}
