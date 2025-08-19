package com.dms.kalari.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.dms.kalari.common.BaseMapper;
import com.dms.kalari.dto.DocumentsDTO;
import com.dms.kalari.entity.MisDocuments;
import com.dms.kalari.entity.MisDocumentsView;

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
