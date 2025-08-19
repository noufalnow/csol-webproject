package com.dms.kalari.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.dms.kalari.common.BaseMapper;
import com.dms.kalari.dto.PropertyPayOptionDTO;
import com.dms.kalari.entity.MisPropertyPayOption;

@Mapper(componentModel = "spring")
public interface MisPropertyPayOptionMapper extends BaseMapper<MisPropertyPayOption, PropertyPayOptionDTO> {
    MisPropertyPayOptionMapper INSTANCE = Mappers.getMapper(MisPropertyPayOptionMapper.class);
}
