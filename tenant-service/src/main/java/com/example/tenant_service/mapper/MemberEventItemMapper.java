package com.example.tenant_service.mapper;

import com.example.tenant_service.common.BaseMapper;
import com.example.tenant_service.dto.MemberEventItemDTO;
import com.example.tenant_service.entity.MemberEventItem;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MemberEventItemMapper extends BaseMapper<MemberEventItem, MemberEventItemDTO> {
    MemberEventItemMapper INSTANCE = Mappers.getMapper(MemberEventItemMapper.class);
    
    // MapStruct will automatically map fields with matching names
    // No need for custom mappings if field names match
}