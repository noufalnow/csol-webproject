package com.example.tenant_service.mapper;

import com.example.tenant_service.dto.DocumentDTO;
import com.example.tenant_service.entity.MisDocuments;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-23T11:36:48+0530",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.37.0.v20240215-1558, environment: Java 17.0.12 (Eclipse Adoptium)"
)
@Component
public class MisDocumentsMapperImpl implements MisDocumentsMapper {

    @Override
    public DocumentDTO toDTO(MisDocuments entity) {
        if ( entity == null ) {
            return null;
        }

        DocumentDTO documentDTO = new DocumentDTO();

        documentDTO.setActive( entity.getActive() );
        documentDTO.setDeleted( map( entity.getDeleted() ) );
        documentDTO.setTCreated( entity.getTCreated() );
        documentDTO.setTDeleted( entity.getTDeleted() );
        documentDTO.setTModified( entity.getTModified() );
        documentDTO.setUCreated( entity.getUCreated() );
        documentDTO.setUDeleted( entity.getUDeleted() );
        documentDTO.setUModified( entity.getUModified() );
        documentDTO.setDocAlertDays( entity.getDocAlertDays() );
        documentDTO.setDocAmount( entity.getDocAmount() );
        documentDTO.setDocApplyDate( entity.getDocApplyDate() );
        documentDTO.setDocDesc( entity.getDocDesc() );
        documentDTO.setDocExpiryDate( entity.getDocExpiryDate() );
        documentDTO.setDocId( entity.getDocId() );
        documentDTO.setDocIssueAuth( entity.getDocIssueAuth() );
        documentDTO.setDocIssueDate( entity.getDocIssueDate() );
        documentDTO.setDocNo( entity.getDocNo() );
        documentDTO.setDocPaydet( entity.getDocPaydet() );
        documentDTO.setDocRefId( entity.getDocRefId() );
        documentDTO.setDocRefType( entity.getDocRefType() );
        documentDTO.setDocRemarks( entity.getDocRemarks() );
        documentDTO.setDocTax( entity.getDocTax() );
        documentDTO.setDocTntId( entity.getDocTntId() );
        documentDTO.setDocType( entity.getDocType() );

        return documentDTO;
    }

    @Override
    public MisDocuments toEntity(DocumentDTO dto) {
        if ( dto == null ) {
            return null;
        }

        MisDocuments misDocuments = new MisDocuments();

        misDocuments.setActive( dto.getActive() );
        misDocuments.setDeleted( map( dto.getDeleted() ) );
        misDocuments.setTCreated( dto.getTCreated() );
        misDocuments.setTDeleted( dto.getTDeleted() );
        misDocuments.setTModified( dto.getTModified() );
        misDocuments.setUCreated( dto.getUCreated() );
        misDocuments.setUDeleted( dto.getUDeleted() );
        misDocuments.setUModified( dto.getUModified() );
        misDocuments.setDocAlertDays( dto.getDocAlertDays() );
        misDocuments.setDocAmount( dto.getDocAmount() );
        misDocuments.setDocApplyDate( dto.getDocApplyDate() );
        misDocuments.setDocDesc( dto.getDocDesc() );
        misDocuments.setDocExpiryDate( dto.getDocExpiryDate() );
        misDocuments.setDocId( dto.getDocId() );
        misDocuments.setDocIssueAuth( dto.getDocIssueAuth() );
        misDocuments.setDocIssueDate( dto.getDocIssueDate() );
        misDocuments.setDocNo( dto.getDocNo() );
        misDocuments.setDocPaydet( dto.getDocPaydet() );
        misDocuments.setDocRefId( dto.getDocRefId() );
        misDocuments.setDocRefType( dto.getDocRefType() );
        misDocuments.setDocRemarks( dto.getDocRemarks() );
        misDocuments.setDocTax( dto.getDocTax() );
        misDocuments.setDocTntId( dto.getDocTntId() );
        misDocuments.setDocType( dto.getDocType() );

        return misDocuments;
    }
}
