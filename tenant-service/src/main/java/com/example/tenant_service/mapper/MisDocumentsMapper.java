package com.example.tenant_service.mapper;

import com.example.tenant_service.common.BaseMapper;
import com.example.tenant_service.dto.DocumentsDTO;
import com.example.tenant_service.entity.MisDocuments;
import com.example.tenant_service.entity.MisDocumentsView;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MisDocumentsMapper extends BaseMapper<MisDocuments, DocumentsDTO> {

    // Map MisDocuments to DocumentDTO
    DocumentsDTO toDTO(MisDocuments misDocuments);
    
    // Map MisDocumentsView to DocumentDTO
    DocumentsDTO toDTO(MisDocumentsView misDocumentsView);

    // Map DocumentDTO to MisDocuments
    MisDocuments toEntity(DocumentsDTO documentDTO);

    // Update MisDocuments fields from DocumentDTO, ignoring null values
    void updateMisDocumentsFromDto(DocumentsDTO documentDTO, @MappingTarget MisDocuments misDocuments);
}
