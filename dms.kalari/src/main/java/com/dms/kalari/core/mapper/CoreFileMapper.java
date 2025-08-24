package com.dms.kalari.core.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.dms.kalari.core.dto.CoreFileDTO;
import com.dms.kalari.core.entity.CoreFile;
import com.dms.kalari.common.BaseMapper;

@Mapper(componentModel = "spring")
public interface CoreFileMapper extends BaseMapper<CoreFile, CoreFileDTO> {
    CoreFileMapper INSTANCE = Mappers.getMapper(CoreFileMapper.class);
}
