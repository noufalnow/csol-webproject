package com.example.tenant_service.mapper;

import com.example.tenant_service.common.BaseMapper;
import com.example.tenant_service.dto.DocumentsViewDTO;
import com.example.tenant_service.entity.MisDocuments;
import com.example.tenant_service.entity.MisDocumentsView;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MisDocumentsViewMapper extends BaseMapper<MisDocuments, DocumentsViewDTO> {

    // Map MisDocuments to DocumentsViewDTO
    DocumentsViewDTO toDTO(MisDocuments misDocuments);
    
    // Map MisDocumentsView to DocumentsViewDTO
    DocumentsViewDTO toDTO(MisDocumentsView misDocumentsView);

    // Map DocumentsViewDTO to MisDocuments
    MisDocuments toEntity(DocumentsViewDTO documentDTO);

    // Update MisDocuments fields from DocumentsViewDTO, ignoring null values
    void updateMisDocumentsFromDto(DocumentsViewDTO documentDTO, @MappingTarget MisDocuments misDocuments);
}
