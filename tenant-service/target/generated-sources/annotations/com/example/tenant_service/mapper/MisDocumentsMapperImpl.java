package com.example.tenant_service.mapper;

import com.example.tenant_service.dto.DocumentsDTO;
import com.example.tenant_service.entity.MisDocuments;
import com.example.tenant_service.entity.MisDocumentsView;
import java.sql.Date;
import java.time.ZoneOffset;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-10-07T14:38:44+0530",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.37.0.v20240215-1558, environment: Java 17.0.12 (Eclipse Adoptium)"
)
@Component
public class MisDocumentsMapperImpl implements MisDocumentsMapper {

    @Override
    public DocumentsDTO toDTO(MisDocuments misDocuments) {
        if ( misDocuments == null ) {
            return null;
        }

        DocumentsDTO documentsDTO = new DocumentsDTO();

        if ( misDocuments.getDocAgreement() != null ) {
            documentsDTO.setDocAgreement( misDocuments.getDocAgreement().longValue() );
        }
        documentsDTO.setDocAlertDays( misDocuments.getDocAlertDays() );
        documentsDTO.setDocAmount( misDocuments.getDocAmount() );
        if ( misDocuments.getDocApplyDate() != null ) {
            documentsDTO.setDocApplyDate( misDocuments.getDocApplyDate().toLocalDate() );
        }
        documentsDTO.setDocDesc( misDocuments.getDocDesc() );
        if ( misDocuments.getDocExpiryDate() != null ) {
            documentsDTO.setDocExpiryDate( misDocuments.getDocExpiryDate().toLocalDate() );
        }
        documentsDTO.setDocId( misDocuments.getDocId() );
        documentsDTO.setDocIssueAuth( misDocuments.getDocIssueAuth() );
        if ( misDocuments.getDocIssueDate() != null ) {
            documentsDTO.setDocIssueDate( misDocuments.getDocIssueDate().toLocalDate() );
        }
        documentsDTO.setDocNo( misDocuments.getDocNo() );
        documentsDTO.setDocPaydet( misDocuments.getDocPaydet() );
        documentsDTO.setDocRefId( misDocuments.getDocRefId() );
        documentsDTO.setDocRefType( misDocuments.getDocRefType() );
        documentsDTO.setDocRemarks( misDocuments.getDocRemarks() );
        documentsDTO.setDocTax( misDocuments.getDocTax() );
        documentsDTO.setDocTntId( misDocuments.getDocTntId() );
        documentsDTO.setDocType( misDocuments.getDocType() );

        return documentsDTO;
    }

    @Override
    public DocumentsDTO toDTO(MisDocumentsView misDocumentsView) {
        if ( misDocumentsView == null ) {
            return null;
        }

        DocumentsDTO documentsDTO = new DocumentsDTO();

        documentsDTO.setDocAgreement( misDocumentsView.getDocAgreement() );
        documentsDTO.setDocAlertDays( misDocumentsView.getDocAlertDays() );
        documentsDTO.setDocAmount( misDocumentsView.getDocAmount() );
        if ( misDocumentsView.getDocApplyDate() != null ) {
            documentsDTO.setDocApplyDate( misDocumentsView.getDocApplyDate().toLocalDate() );
        }
        documentsDTO.setDocDesc( misDocumentsView.getDocDesc() );
        if ( misDocumentsView.getDocExpiryDate() != null ) {
            documentsDTO.setDocExpiryDate( misDocumentsView.getDocExpiryDate().toLocalDate() );
        }
        documentsDTO.setDocId( misDocumentsView.getDocId() );
        documentsDTO.setDocIssueAuth( misDocumentsView.getDocIssueAuth() );
        if ( misDocumentsView.getDocIssueDate() != null ) {
            documentsDTO.setDocIssueDate( misDocumentsView.getDocIssueDate().toLocalDate() );
        }
        documentsDTO.setDocNo( misDocumentsView.getDocNo() );
        documentsDTO.setDocPaydet( misDocumentsView.getDocPaydet() );
        documentsDTO.setDocRefId( misDocumentsView.getDocRefId() );
        documentsDTO.setDocRefType( misDocumentsView.getDocRefType() );
        documentsDTO.setDocRemarks( misDocumentsView.getDocRemarks() );
        documentsDTO.setDocTax( misDocumentsView.getDocTax() );
        documentsDTO.setDocTntId( misDocumentsView.getDocTntId() );
        documentsDTO.setDocType( misDocumentsView.getDocType() );

        return documentsDTO;
    }

    @Override
    public MisDocuments toEntity(DocumentsDTO documentDTO) {
        if ( documentDTO == null ) {
            return null;
        }

        MisDocuments misDocuments = new MisDocuments();

        if ( documentDTO.getDocAgreement() != null ) {
            misDocuments.setDocAgreement( documentDTO.getDocAgreement().shortValue() );
        }
        misDocuments.setDocAlertDays( documentDTO.getDocAlertDays() );
        misDocuments.setDocAmount( documentDTO.getDocAmount() );
        if ( documentDTO.getDocApplyDate() != null ) {
            misDocuments.setDocApplyDate( new Date( documentDTO.getDocApplyDate().atStartOfDay( ZoneOffset.UTC ).toInstant().toEpochMilli() ) );
        }
        misDocuments.setDocDesc( documentDTO.getDocDesc() );
        if ( documentDTO.getDocExpiryDate() != null ) {
            misDocuments.setDocExpiryDate( new Date( documentDTO.getDocExpiryDate().atStartOfDay( ZoneOffset.UTC ).toInstant().toEpochMilli() ) );
        }
        misDocuments.setDocId( documentDTO.getDocId() );
        misDocuments.setDocIssueAuth( documentDTO.getDocIssueAuth() );
        if ( documentDTO.getDocIssueDate() != null ) {
            misDocuments.setDocIssueDate( new Date( documentDTO.getDocIssueDate().atStartOfDay( ZoneOffset.UTC ).toInstant().toEpochMilli() ) );
        }
        misDocuments.setDocNo( documentDTO.getDocNo() );
        misDocuments.setDocPaydet( documentDTO.getDocPaydet() );
        misDocuments.setDocRefId( documentDTO.getDocRefId() );
        misDocuments.setDocRefType( documentDTO.getDocRefType() );
        misDocuments.setDocRemarks( documentDTO.getDocRemarks() );
        misDocuments.setDocTax( documentDTO.getDocTax() );
        misDocuments.setDocTntId( documentDTO.getDocTntId() );
        misDocuments.setDocType( documentDTO.getDocType() );

        return misDocuments;
    }

    @Override
    public void updateMisDocumentsFromDto(DocumentsDTO documentDTO, MisDocuments misDocuments) {
        if ( documentDTO == null ) {
            return;
        }

        if ( documentDTO.getDocAgreement() != null ) {
            misDocuments.setDocAgreement( documentDTO.getDocAgreement().shortValue() );
        }
        if ( documentDTO.getDocAlertDays() != null ) {
            misDocuments.setDocAlertDays( documentDTO.getDocAlertDays() );
        }
        if ( documentDTO.getDocAmount() != null ) {
            misDocuments.setDocAmount( documentDTO.getDocAmount() );
        }
        if ( documentDTO.getDocApplyDate() != null ) {
            misDocuments.setDocApplyDate( new Date( documentDTO.getDocApplyDate().atStartOfDay( ZoneOffset.UTC ).toInstant().toEpochMilli() ) );
        }
        if ( documentDTO.getDocDesc() != null ) {
            misDocuments.setDocDesc( documentDTO.getDocDesc() );
        }
        if ( documentDTO.getDocExpiryDate() != null ) {
            misDocuments.setDocExpiryDate( new Date( documentDTO.getDocExpiryDate().atStartOfDay( ZoneOffset.UTC ).toInstant().toEpochMilli() ) );
        }
        if ( documentDTO.getDocId() != null ) {
            misDocuments.setDocId( documentDTO.getDocId() );
        }
        if ( documentDTO.getDocIssueAuth() != null ) {
            misDocuments.setDocIssueAuth( documentDTO.getDocIssueAuth() );
        }
        if ( documentDTO.getDocIssueDate() != null ) {
            misDocuments.setDocIssueDate( new Date( documentDTO.getDocIssueDate().atStartOfDay( ZoneOffset.UTC ).toInstant().toEpochMilli() ) );
        }
        if ( documentDTO.getDocNo() != null ) {
            misDocuments.setDocNo( documentDTO.getDocNo() );
        }
        if ( documentDTO.getDocPaydet() != null ) {
            misDocuments.setDocPaydet( documentDTO.getDocPaydet() );
        }
        if ( documentDTO.getDocRefId() != null ) {
            misDocuments.setDocRefId( documentDTO.getDocRefId() );
        }
        if ( documentDTO.getDocRefType() != null ) {
            misDocuments.setDocRefType( documentDTO.getDocRefType() );
        }
        if ( documentDTO.getDocRemarks() != null ) {
            misDocuments.setDocRemarks( documentDTO.getDocRemarks() );
        }
        if ( documentDTO.getDocTax() != null ) {
            misDocuments.setDocTax( documentDTO.getDocTax() );
        }
        if ( documentDTO.getDocTntId() != null ) {
            misDocuments.setDocTntId( documentDTO.getDocTntId() );
        }
        if ( documentDTO.getDocType() != null ) {
            misDocuments.setDocType( documentDTO.getDocType() );
        }
    }
}
