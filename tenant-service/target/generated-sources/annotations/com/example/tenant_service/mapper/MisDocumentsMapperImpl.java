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
    date = "2025-05-22T20:55:15+0530",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.13 (Ubuntu)"
)
@Component
public class MisDocumentsMapperImpl implements MisDocumentsMapper {

    @Override
    public DocumentsDTO toDTO(MisDocuments misDocuments) {
        if ( misDocuments == null ) {
            return null;
        }

        DocumentsDTO documentsDTO = new DocumentsDTO();

        documentsDTO.setDocId( misDocuments.getDocId() );
        documentsDTO.setDocType( misDocuments.getDocType() );
        documentsDTO.setDocRefType( misDocuments.getDocRefType() );
        documentsDTO.setDocRefId( misDocuments.getDocRefId() );
        documentsDTO.setDocNo( misDocuments.getDocNo() );
        documentsDTO.setDocDesc( misDocuments.getDocDesc() );
        documentsDTO.setDocRemarks( misDocuments.getDocRemarks() );
        documentsDTO.setDocIssueAuth( misDocuments.getDocIssueAuth() );
        if ( misDocuments.getDocApplyDate() != null ) {
            documentsDTO.setDocApplyDate( misDocuments.getDocApplyDate().toLocalDate() );
        }
        if ( misDocuments.getDocIssueDate() != null ) {
            documentsDTO.setDocIssueDate( misDocuments.getDocIssueDate().toLocalDate() );
        }
        if ( misDocuments.getDocExpiryDate() != null ) {
            documentsDTO.setDocExpiryDate( misDocuments.getDocExpiryDate().toLocalDate() );
        }
        documentsDTO.setDocAlertDays( misDocuments.getDocAlertDays() );
        documentsDTO.setDocAmount( misDocuments.getDocAmount() );
        documentsDTO.setDocTax( misDocuments.getDocTax() );
        documentsDTO.setDocPaydet( misDocuments.getDocPaydet() );
        documentsDTO.setDocTntId( misDocuments.getDocTntId() );
        if ( misDocuments.getDocAgreement() != null ) {
            documentsDTO.setDocAgreement( misDocuments.getDocAgreement().longValue() );
        }

        return documentsDTO;
    }

    @Override
    public DocumentsDTO toDTO(MisDocumentsView misDocumentsView) {
        if ( misDocumentsView == null ) {
            return null;
        }

        DocumentsDTO documentsDTO = new DocumentsDTO();

        documentsDTO.setDocId( misDocumentsView.getDocId() );
        documentsDTO.setDocType( misDocumentsView.getDocType() );
        documentsDTO.setDocRefType( misDocumentsView.getDocRefType() );
        documentsDTO.setDocRefId( misDocumentsView.getDocRefId() );
        documentsDTO.setDocNo( misDocumentsView.getDocNo() );
        documentsDTO.setDocDesc( misDocumentsView.getDocDesc() );
        documentsDTO.setDocRemarks( misDocumentsView.getDocRemarks() );
        documentsDTO.setDocIssueAuth( misDocumentsView.getDocIssueAuth() );
        if ( misDocumentsView.getDocApplyDate() != null ) {
            documentsDTO.setDocApplyDate( misDocumentsView.getDocApplyDate().toLocalDate() );
        }
        if ( misDocumentsView.getDocIssueDate() != null ) {
            documentsDTO.setDocIssueDate( misDocumentsView.getDocIssueDate().toLocalDate() );
        }
        if ( misDocumentsView.getDocExpiryDate() != null ) {
            documentsDTO.setDocExpiryDate( misDocumentsView.getDocExpiryDate().toLocalDate() );
        }
        documentsDTO.setDocAlertDays( misDocumentsView.getDocAlertDays() );
        documentsDTO.setDocAmount( misDocumentsView.getDocAmount() );
        documentsDTO.setDocTax( misDocumentsView.getDocTax() );
        documentsDTO.setDocPaydet( misDocumentsView.getDocPaydet() );
        documentsDTO.setDocTntId( misDocumentsView.getDocTntId() );
        documentsDTO.setDocAgreement( misDocumentsView.getDocAgreement() );

        return documentsDTO;
    }

    @Override
    public MisDocuments toEntity(DocumentsDTO documentDTO) {
        if ( documentDTO == null ) {
            return null;
        }

        MisDocuments misDocuments = new MisDocuments();

        misDocuments.setDocId( documentDTO.getDocId() );
        misDocuments.setDocType( documentDTO.getDocType() );
        misDocuments.setDocRefType( documentDTO.getDocRefType() );
        misDocuments.setDocRefId( documentDTO.getDocRefId() );
        misDocuments.setDocNo( documentDTO.getDocNo() );
        misDocuments.setDocDesc( documentDTO.getDocDesc() );
        misDocuments.setDocRemarks( documentDTO.getDocRemarks() );
        misDocuments.setDocIssueAuth( documentDTO.getDocIssueAuth() );
        if ( documentDTO.getDocApplyDate() != null ) {
            misDocuments.setDocApplyDate( new Date( documentDTO.getDocApplyDate().atStartOfDay( ZoneOffset.UTC ).toInstant().toEpochMilli() ) );
        }
        if ( documentDTO.getDocIssueDate() != null ) {
            misDocuments.setDocIssueDate( new Date( documentDTO.getDocIssueDate().atStartOfDay( ZoneOffset.UTC ).toInstant().toEpochMilli() ) );
        }
        if ( documentDTO.getDocExpiryDate() != null ) {
            misDocuments.setDocExpiryDate( new Date( documentDTO.getDocExpiryDate().atStartOfDay( ZoneOffset.UTC ).toInstant().toEpochMilli() ) );
        }
        misDocuments.setDocAlertDays( documentDTO.getDocAlertDays() );
        misDocuments.setDocAmount( documentDTO.getDocAmount() );
        misDocuments.setDocTax( documentDTO.getDocTax() );
        misDocuments.setDocPaydet( documentDTO.getDocPaydet() );
        misDocuments.setDocTntId( documentDTO.getDocTntId() );
        if ( documentDTO.getDocAgreement() != null ) {
            misDocuments.setDocAgreement( documentDTO.getDocAgreement().shortValue() );
        }

        return misDocuments;
    }

    @Override
    public void updateMisDocumentsFromDto(DocumentsDTO documentDTO, MisDocuments misDocuments) {
        if ( documentDTO == null ) {
            return;
        }

        if ( documentDTO.getDocId() != null ) {
            misDocuments.setDocId( documentDTO.getDocId() );
        }
        if ( documentDTO.getDocType() != null ) {
            misDocuments.setDocType( documentDTO.getDocType() );
        }
        if ( documentDTO.getDocRefType() != null ) {
            misDocuments.setDocRefType( documentDTO.getDocRefType() );
        }
        if ( documentDTO.getDocRefId() != null ) {
            misDocuments.setDocRefId( documentDTO.getDocRefId() );
        }
        if ( documentDTO.getDocNo() != null ) {
            misDocuments.setDocNo( documentDTO.getDocNo() );
        }
        if ( documentDTO.getDocDesc() != null ) {
            misDocuments.setDocDesc( documentDTO.getDocDesc() );
        }
        if ( documentDTO.getDocRemarks() != null ) {
            misDocuments.setDocRemarks( documentDTO.getDocRemarks() );
        }
        if ( documentDTO.getDocIssueAuth() != null ) {
            misDocuments.setDocIssueAuth( documentDTO.getDocIssueAuth() );
        }
        if ( documentDTO.getDocApplyDate() != null ) {
            misDocuments.setDocApplyDate( new Date( documentDTO.getDocApplyDate().atStartOfDay( ZoneOffset.UTC ).toInstant().toEpochMilli() ) );
        }
        if ( documentDTO.getDocIssueDate() != null ) {
            misDocuments.setDocIssueDate( new Date( documentDTO.getDocIssueDate().atStartOfDay( ZoneOffset.UTC ).toInstant().toEpochMilli() ) );
        }
        if ( documentDTO.getDocExpiryDate() != null ) {
            misDocuments.setDocExpiryDate( new Date( documentDTO.getDocExpiryDate().atStartOfDay( ZoneOffset.UTC ).toInstant().toEpochMilli() ) );
        }
        if ( documentDTO.getDocAlertDays() != null ) {
            misDocuments.setDocAlertDays( documentDTO.getDocAlertDays() );
        }
        if ( documentDTO.getDocAmount() != null ) {
            misDocuments.setDocAmount( documentDTO.getDocAmount() );
        }
        if ( documentDTO.getDocTax() != null ) {
            misDocuments.setDocTax( documentDTO.getDocTax() );
        }
        if ( documentDTO.getDocPaydet() != null ) {
            misDocuments.setDocPaydet( documentDTO.getDocPaydet() );
        }
        if ( documentDTO.getDocTntId() != null ) {
            misDocuments.setDocTntId( documentDTO.getDocTntId() );
        }
        if ( documentDTO.getDocAgreement() != null ) {
            misDocuments.setDocAgreement( documentDTO.getDocAgreement().shortValue() );
        }
    }
}
