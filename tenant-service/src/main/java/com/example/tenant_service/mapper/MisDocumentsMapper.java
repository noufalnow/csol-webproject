package com.example.tenant_service.mapper;

import com.example.tenant_service.common.BaseMapper;
import com.example.tenant_service.dto.DocumentDTO;
import com.example.tenant_service.entity.MisDocuments;
import com.example.tenant_service.entity.MisDocumentsView;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MisDocumentsMapper extends BaseMapper<MisDocuments, DocumentDTO> {

    // Map MisDocuments to DocumentDTO
    DocumentDTO toDTO(MisDocuments misDocuments);
    
    // Map MisDocumentsView to DocumentDTO
    DocumentDTO toDTO(MisDocumentsView misDocumentsView);

    // Map DocumentDTO to MisDocuments
    MisDocuments toEntity(DocumentDTO documentDTO);

    // Update MisDocuments fields from DocumentDTO, ignoring null values
    void updateMisDocumentsFromDto(DocumentDTO documentDTO, @MappingTarget MisDocuments misDocuments);
}
