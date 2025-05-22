package com.example.tenant_service.mapper;

import com.example.tenant_service.dto.DocumentsViewDTO;
import com.example.tenant_service.entity.MisDocuments;
import com.example.tenant_service.entity.MisDocumentsView;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-22T20:55:15+0530",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.13 (Ubuntu)"
)
@Component
public class MisDocumentsViewMapperImpl implements MisDocumentsViewMapper {

    @Override
    public DocumentsViewDTO toDTO(MisDocuments misDocuments) {
        if ( misDocuments == null ) {
            return null;
        }

        DocumentsViewDTO documentsViewDTO = new DocumentsViewDTO();

        documentsViewDTO.setDocId( misDocuments.getDocId() );
        documentsViewDTO.setDocType( misDocuments.getDocType() );
        documentsViewDTO.setDocRefType( misDocuments.getDocRefType() );
        documentsViewDTO.setDocRefId( misDocuments.getDocRefId() );
        documentsViewDTO.setDocNo( misDocuments.getDocNo() );
        documentsViewDTO.setDocDesc( misDocuments.getDocDesc() );
        documentsViewDTO.setDocRemarks( misDocuments.getDocRemarks() );
        documentsViewDTO.setDocIssueAuth( misDocuments.getDocIssueAuth() );
        documentsViewDTO.setDocApplyDate( misDocuments.getDocApplyDate() );
        documentsViewDTO.setDocIssueDate( misDocuments.getDocIssueDate() );
        documentsViewDTO.setDocExpiryDate( misDocuments.getDocExpiryDate() );
        documentsViewDTO.setDocAlertDays( misDocuments.getDocAlertDays() );
        documentsViewDTO.setDocAmount( misDocuments.getDocAmount() );
        documentsViewDTO.setDocTax( misDocuments.getDocTax() );
        documentsViewDTO.setDocPaydet( misDocuments.getDocPaydet() );
        documentsViewDTO.setDocTntId( misDocuments.getDocTntId() );
        if ( misDocuments.getDocAgreement() != null ) {
            documentsViewDTO.setDocAgreement( misDocuments.getDocAgreement().longValue() );
        }
        if ( misDocuments.getDeleted() != null ) {
            documentsViewDTO.setDeleted( misDocuments.getDeleted() );
        }

        return documentsViewDTO;
    }

    @Override
    public DocumentsViewDTO toDTO(MisDocumentsView misDocumentsView) {
        if ( misDocumentsView == null ) {
            return null;
        }

        DocumentsViewDTO documentsViewDTO = new DocumentsViewDTO();

        documentsViewDTO.setDocId( misDocumentsView.getDocId() );
        documentsViewDTO.setDocType( misDocumentsView.getDocType() );
        documentsViewDTO.setDocRefType( misDocumentsView.getDocRefType() );
        documentsViewDTO.setDocRefId( misDocumentsView.getDocRefId() );
        documentsViewDTO.setDocNo( misDocumentsView.getDocNo() );
        documentsViewDTO.setDocDesc( misDocumentsView.getDocDesc() );
        documentsViewDTO.setDocRemarks( misDocumentsView.getDocRemarks() );
        documentsViewDTO.setDocIssueAuth( misDocumentsView.getDocIssueAuth() );
        documentsViewDTO.setDocApplyDate( misDocumentsView.getDocApplyDate() );
        documentsViewDTO.setDocIssueDate( misDocumentsView.getDocIssueDate() );
        documentsViewDTO.setDocExpiryDate( misDocumentsView.getDocExpiryDate() );
        documentsViewDTO.setDocAlertDays( misDocumentsView.getDocAlertDays() );
        documentsViewDTO.setDocAmount( misDocumentsView.getDocAmount() );
        documentsViewDTO.setDocTax( misDocumentsView.getDocTax() );
        documentsViewDTO.setDocPaydet( misDocumentsView.getDocPaydet() );
        documentsViewDTO.setDocTntId( misDocumentsView.getDocTntId() );
        documentsViewDTO.setDocAgreement( misDocumentsView.getDocAgreement() );
        documentsViewDTO.setDeleted( misDocumentsView.isDeleted() );
        documentsViewDTO.setTenantFullName( misDocumentsView.getTenantFullName() );
        documentsViewDTO.setTenantCompanyName( misDocumentsView.getTenantCompanyName() );
        documentsViewDTO.setTenantPhone( misDocumentsView.getTenantPhone() );
        documentsViewDTO.setTenantTele( misDocumentsView.getTenantTele() );
        documentsViewDTO.setTenantIdNo( misDocumentsView.getTenantIdNo() );
        documentsViewDTO.setTenantCrno( misDocumentsView.getTenantCrno() );
        documentsViewDTO.setTenantExpat( misDocumentsView.getTenantExpat() );
        documentsViewDTO.setTenantAgrType( misDocumentsView.getTenantAgrType() );
        documentsViewDTO.setTenantDocId( misDocumentsView.getTenantDocId() );
        documentsViewDTO.setPropId( misDocumentsView.getPropId() );
        documentsViewDTO.setPropNo( misDocumentsView.getPropNo() );
        documentsViewDTO.setPropName( misDocumentsView.getPropName() );
        documentsViewDTO.setPropFileno( misDocumentsView.getPropFileno() );
        documentsViewDTO.setPropBuilding( misDocumentsView.getPropBuilding() );
        documentsViewDTO.setPropResponsible( misDocumentsView.getPropResponsible() );
        documentsViewDTO.setPropRemarks( misDocumentsView.getPropRemarks() );
        documentsViewDTO.setPropCat( misDocumentsView.getPropCat() );
        documentsViewDTO.setPropType( misDocumentsView.getPropType() );
        documentsViewDTO.setPropLevel( misDocumentsView.getPropLevel() );
        documentsViewDTO.setPropElecMeter( misDocumentsView.getPropElecMeter() );
        documentsViewDTO.setPropWater( misDocumentsView.getPropWater() );
        documentsViewDTO.setPropBuildingType( misDocumentsView.getPropBuildingType() );
        documentsViewDTO.setPropStatus( misDocumentsView.getPropStatus() );
        documentsViewDTO.setPropElecAccount( misDocumentsView.getPropElecAccount() );
        documentsViewDTO.setPropElecRecharge( misDocumentsView.getPropElecRecharge() );
        documentsViewDTO.setPropAccount( misDocumentsView.getPropAccount() );

        return documentsViewDTO;
    }

    @Override
    public MisDocuments toEntity(DocumentsViewDTO documentDTO) {
        if ( documentDTO == null ) {
            return null;
        }

        MisDocuments misDocuments = new MisDocuments();

        misDocuments.setDeleted( documentDTO.isDeleted() );
        misDocuments.setDocId( documentDTO.getDocId() );
        misDocuments.setDocType( documentDTO.getDocType() );
        misDocuments.setDocRefType( documentDTO.getDocRefType() );
        misDocuments.setDocRefId( documentDTO.getDocRefId() );
        misDocuments.setDocNo( documentDTO.getDocNo() );
        misDocuments.setDocDesc( documentDTO.getDocDesc() );
        misDocuments.setDocRemarks( documentDTO.getDocRemarks() );
        misDocuments.setDocIssueAuth( documentDTO.getDocIssueAuth() );
        misDocuments.setDocApplyDate( documentDTO.getDocApplyDate() );
        misDocuments.setDocIssueDate( documentDTO.getDocIssueDate() );
        misDocuments.setDocExpiryDate( documentDTO.getDocExpiryDate() );
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
    public void updateMisDocumentsFromDto(DocumentsViewDTO documentDTO, MisDocuments misDocuments) {
        if ( documentDTO == null ) {
            return;
        }

        misDocuments.setDeleted( documentDTO.isDeleted() );
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
            misDocuments.setDocApplyDate( documentDTO.getDocApplyDate() );
        }
        if ( documentDTO.getDocIssueDate() != null ) {
            misDocuments.setDocIssueDate( documentDTO.getDocIssueDate() );
        }
        if ( documentDTO.getDocExpiryDate() != null ) {
            misDocuments.setDocExpiryDate( documentDTO.getDocExpiryDate() );
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
