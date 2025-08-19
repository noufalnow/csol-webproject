package com.dms.kalari.mapper;

import com.dms.kalari.dto.DocumentsViewDTO;
import com.dms.kalari.entity.MisDocuments;
import com.dms.kalari.entity.MisDocumentsView;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-19T13:35:09+0530",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250523-0729, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class MisDocumentsViewMapperImpl implements MisDocumentsViewMapper {

    @Override
    public DocumentsViewDTO toDTO(MisDocuments misDocuments) {
        if ( misDocuments == null ) {
            return null;
        }

        DocumentsViewDTO documentsViewDTO = new DocumentsViewDTO();

        if ( misDocuments.getDeleted() != null ) {
            documentsViewDTO.setDeleted( misDocuments.getDeleted() );
        }
        if ( misDocuments.getDocAgreement() != null ) {
            documentsViewDTO.setDocAgreement( misDocuments.getDocAgreement().longValue() );
        }
        documentsViewDTO.setDocAlertDays( misDocuments.getDocAlertDays() );
        documentsViewDTO.setDocAmount( misDocuments.getDocAmount() );
        documentsViewDTO.setDocApplyDate( misDocuments.getDocApplyDate() );
        documentsViewDTO.setDocDesc( misDocuments.getDocDesc() );
        documentsViewDTO.setDocExpiryDate( misDocuments.getDocExpiryDate() );
        documentsViewDTO.setDocId( misDocuments.getDocId() );
        documentsViewDTO.setDocIssueAuth( misDocuments.getDocIssueAuth() );
        documentsViewDTO.setDocIssueDate( misDocuments.getDocIssueDate() );
        documentsViewDTO.setDocNo( misDocuments.getDocNo() );
        documentsViewDTO.setDocPaydet( misDocuments.getDocPaydet() );
        documentsViewDTO.setDocRefId( misDocuments.getDocRefId() );
        documentsViewDTO.setDocRefType( misDocuments.getDocRefType() );
        documentsViewDTO.setDocRemarks( misDocuments.getDocRemarks() );
        documentsViewDTO.setDocTax( misDocuments.getDocTax() );
        documentsViewDTO.setDocTntId( misDocuments.getDocTntId() );
        documentsViewDTO.setDocType( misDocuments.getDocType() );

        return documentsViewDTO;
    }

    @Override
    public DocumentsViewDTO toDTO(MisDocumentsView misDocumentsView) {
        if ( misDocumentsView == null ) {
            return null;
        }

        DocumentsViewDTO documentsViewDTO = new DocumentsViewDTO();

        documentsViewDTO.setDeleted( misDocumentsView.isDeleted() );
        documentsViewDTO.setDocAgreement( misDocumentsView.getDocAgreement() );
        documentsViewDTO.setDocAlertDays( misDocumentsView.getDocAlertDays() );
        documentsViewDTO.setDocAmount( misDocumentsView.getDocAmount() );
        documentsViewDTO.setDocApplyDate( misDocumentsView.getDocApplyDate() );
        documentsViewDTO.setDocDesc( misDocumentsView.getDocDesc() );
        documentsViewDTO.setDocExpiryDate( misDocumentsView.getDocExpiryDate() );
        documentsViewDTO.setDocId( misDocumentsView.getDocId() );
        documentsViewDTO.setDocIssueAuth( misDocumentsView.getDocIssueAuth() );
        documentsViewDTO.setDocIssueDate( misDocumentsView.getDocIssueDate() );
        documentsViewDTO.setDocNo( misDocumentsView.getDocNo() );
        documentsViewDTO.setDocPaydet( misDocumentsView.getDocPaydet() );
        documentsViewDTO.setDocRefId( misDocumentsView.getDocRefId() );
        documentsViewDTO.setDocRefType( misDocumentsView.getDocRefType() );
        documentsViewDTO.setDocRemarks( misDocumentsView.getDocRemarks() );
        documentsViewDTO.setDocTax( misDocumentsView.getDocTax() );
        documentsViewDTO.setDocTntId( misDocumentsView.getDocTntId() );
        documentsViewDTO.setDocType( misDocumentsView.getDocType() );
        documentsViewDTO.setPropAccount( misDocumentsView.getPropAccount() );
        documentsViewDTO.setPropBuilding( misDocumentsView.getPropBuilding() );
        documentsViewDTO.setPropBuildingType( misDocumentsView.getPropBuildingType() );
        documentsViewDTO.setPropCat( misDocumentsView.getPropCat() );
        documentsViewDTO.setPropElecAccount( misDocumentsView.getPropElecAccount() );
        documentsViewDTO.setPropElecMeter( misDocumentsView.getPropElecMeter() );
        documentsViewDTO.setPropElecRecharge( misDocumentsView.getPropElecRecharge() );
        documentsViewDTO.setPropFileno( misDocumentsView.getPropFileno() );
        documentsViewDTO.setPropId( misDocumentsView.getPropId() );
        documentsViewDTO.setPropLevel( misDocumentsView.getPropLevel() );
        documentsViewDTO.setPropName( misDocumentsView.getPropName() );
        documentsViewDTO.setPropNo( misDocumentsView.getPropNo() );
        documentsViewDTO.setPropRemarks( misDocumentsView.getPropRemarks() );
        documentsViewDTO.setPropResponsible( misDocumentsView.getPropResponsible() );
        documentsViewDTO.setPropStatus( misDocumentsView.getPropStatus() );
        documentsViewDTO.setPropType( misDocumentsView.getPropType() );
        documentsViewDTO.setPropWater( misDocumentsView.getPropWater() );
        documentsViewDTO.setTenantAgrType( misDocumentsView.getTenantAgrType() );
        documentsViewDTO.setTenantCompanyName( misDocumentsView.getTenantCompanyName() );
        documentsViewDTO.setTenantCrno( misDocumentsView.getTenantCrno() );
        documentsViewDTO.setTenantDocId( misDocumentsView.getTenantDocId() );
        documentsViewDTO.setTenantExpat( misDocumentsView.getTenantExpat() );
        documentsViewDTO.setTenantFullName( misDocumentsView.getTenantFullName() );
        documentsViewDTO.setTenantIdNo( misDocumentsView.getTenantIdNo() );
        documentsViewDTO.setTenantPhone( misDocumentsView.getTenantPhone() );
        documentsViewDTO.setTenantTele( misDocumentsView.getTenantTele() );

        return documentsViewDTO;
    }

    @Override
    public MisDocuments toEntity(DocumentsViewDTO documentDTO) {
        if ( documentDTO == null ) {
            return null;
        }

        MisDocuments misDocuments = new MisDocuments();

        misDocuments.setDeleted( documentDTO.isDeleted() );
        if ( documentDTO.getDocAgreement() != null ) {
            misDocuments.setDocAgreement( documentDTO.getDocAgreement().shortValue() );
        }
        misDocuments.setDocAlertDays( documentDTO.getDocAlertDays() );
        misDocuments.setDocAmount( documentDTO.getDocAmount() );
        misDocuments.setDocApplyDate( documentDTO.getDocApplyDate() );
        misDocuments.setDocDesc( documentDTO.getDocDesc() );
        misDocuments.setDocExpiryDate( documentDTO.getDocExpiryDate() );
        misDocuments.setDocId( documentDTO.getDocId() );
        misDocuments.setDocIssueAuth( documentDTO.getDocIssueAuth() );
        misDocuments.setDocIssueDate( documentDTO.getDocIssueDate() );
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
    public void updateMisDocumentsFromDto(DocumentsViewDTO documentDTO, MisDocuments misDocuments) {
        if ( documentDTO == null ) {
            return;
        }

        misDocuments.setDeleted( documentDTO.isDeleted() );
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
            misDocuments.setDocApplyDate( documentDTO.getDocApplyDate() );
        }
        if ( documentDTO.getDocDesc() != null ) {
            misDocuments.setDocDesc( documentDTO.getDocDesc() );
        }
        if ( documentDTO.getDocExpiryDate() != null ) {
            misDocuments.setDocExpiryDate( documentDTO.getDocExpiryDate() );
        }
        if ( documentDTO.getDocId() != null ) {
            misDocuments.setDocId( documentDTO.getDocId() );
        }
        if ( documentDTO.getDocIssueAuth() != null ) {
            misDocuments.setDocIssueAuth( documentDTO.getDocIssueAuth() );
        }
        if ( documentDTO.getDocIssueDate() != null ) {
            misDocuments.setDocIssueDate( documentDTO.getDocIssueDate() );
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
