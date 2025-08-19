package com.dms.kalari.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.dms.kalari.common.BaseMapper;
import com.dms.kalari.dto.DocumentsViewDTO;
import com.dms.kalari.entity.MisDocuments;
import com.dms.kalari.entity.MisDocumentsView;

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
