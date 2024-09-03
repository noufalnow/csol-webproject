package com.example.tenant_service.mapper;

import com.example.tenant_service.common.BaseMapper;
import com.example.tenant_service.dto.DocumentDTO;
import com.example.tenant_service.entity.MisDocuments;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MisDocumentsMapper extends BaseMapper<MisDocuments, DocumentDTO> {
    MisDocumentsMapper INSTANCE = Mappers.getMapper(MisDocumentsMapper.class);

    // Custom mappings for specific fields can be added here if necessary
}
