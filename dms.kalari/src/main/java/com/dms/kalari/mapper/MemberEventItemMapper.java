package com.dms.kalari.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.dms.kalari.common.BaseMapper;
import com.dms.kalari.dto.MemberEventItemDTO;
import com.dms.kalari.entity.MemberEventItem;

@Mapper(componentModel = "spring")
public interface MemberEventItemMapper extends BaseMapper<MemberEventItem, MemberEventItemDTO> {
    MemberEventItemMapper INSTANCE = Mappers.getMapper(MemberEventItemMapper.class);
    
    // MapStruct will automatically map fields with matching names
    // No need for custom mappings if field names match
}