package com.example.tenant_service.mapper;

import com.example.tenant_service.dto.DocumentDTO;
import com.example.tenant_service.entity.MisDocuments;
import com.example.tenant_service.entity.MisDocumentsView;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-24T23:33:37+0530",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.37.0.v20240215-1558, environment: Java 17.0.12 (Eclipse Adoptium)"
)
@Component
public class MisDocumentsMapperImpl implements MisDocumentsMapper {

    @Override
    public DocumentDTO toDTO(MisDocuments misDocuments) {
        if ( misDocuments == null ) {
            return null;
        }

        DocumentDTO documentDTO = new DocumentDTO();

        if ( misDocuments.getDeleted() != null ) {
            documentDTO.setDeleted( misDocuments.getDeleted() );
        }
        documentDTO.setDocAlertDays( misDocuments.getDocAlertDays() );
        documentDTO.setDocAmount( misDocuments.getDocAmount() );
        documentDTO.setDocApplyDate( misDocuments.getDocApplyDate() );
        documentDTO.setDocDesc( misDocuments.getDocDesc() );
        documentDTO.setDocExpiryDate( misDocuments.getDocExpiryDate() );
        documentDTO.setDocId( misDocuments.getDocId() );
        documentDTO.setDocIssueAuth( misDocuments.getDocIssueAuth() );
        documentDTO.setDocIssueDate( misDocuments.getDocIssueDate() );
        documentDTO.setDocNo( misDocuments.getDocNo() );
        documentDTO.setDocPaydet( misDocuments.getDocPaydet() );
        documentDTO.setDocRefId( misDocuments.getDocRefId() );
        documentDTO.setDocRefType( misDocuments.getDocRefType() );
        documentDTO.setDocRemarks( misDocuments.getDocRemarks() );
        documentDTO.setDocTax( misDocuments.getDocTax() );
        documentDTO.setDocTntId( misDocuments.getDocTntId() );
        documentDTO.setDocType( misDocuments.getDocType() );

        return documentDTO;
    }

    @Override
    public DocumentDTO toDTO(MisDocumentsView misDocumentsView) {
        if ( misDocumentsView == null ) {
            return null;
        }

        DocumentDTO documentDTO = new DocumentDTO();

        documentDTO.setDeleted( misDocumentsView.isDeleted() );
        documentDTO.setDocAlertDays( misDocumentsView.getDocAlertDays() );
        documentDTO.setDocAmount( misDocumentsView.getDocAmount() );
        documentDTO.setDocApplyDate( misDocumentsView.getDocApplyDate() );
        documentDTO.setDocDesc( misDocumentsView.getDocDesc() );
        documentDTO.setDocExpiryDate( misDocumentsView.getDocExpiryDate() );
        documentDTO.setDocId( misDocumentsView.getDocId() );
        documentDTO.setDocIssueAuth( misDocumentsView.getDocIssueAuth() );
        documentDTO.setDocIssueDate( misDocumentsView.getDocIssueDate() );
        documentDTO.setDocNo( misDocumentsView.getDocNo() );
        documentDTO.setDocPaydet( misDocumentsView.getDocPaydet() );
        documentDTO.setDocRefId( misDocumentsView.getDocRefId() );
        documentDTO.setDocRefType( misDocumentsView.getDocRefType() );
        documentDTO.setDocRemarks( misDocumentsView.getDocRemarks() );
        documentDTO.setDocTax( misDocumentsView.getDocTax() );
        documentDTO.setDocTntId( misDocumentsView.getDocTntId() );
        documentDTO.setDocType( misDocumentsView.getDocType() );
        documentDTO.setPropAccount( misDocumentsView.getPropAccount() );
        documentDTO.setPropBuilding( misDocumentsView.getPropBuilding() );
        documentDTO.setPropBuildingType( misDocumentsView.getPropBuildingType() );
        documentDTO.setPropCat( misDocumentsView.getPropCat() );
        documentDTO.setPropElecAccount( misDocumentsView.getPropElecAccount() );
        documentDTO.setPropElecMeter( misDocumentsView.getPropElecMeter() );
        documentDTO.setPropElecRecharge( misDocumentsView.getPropElecRecharge() );
        documentDTO.setPropFileno( misDocumentsView.getPropFileno() );
        documentDTO.setPropId( misDocumentsView.getPropId() );
        documentDTO.setPropLevel( misDocumentsView.getPropLevel() );
        documentDTO.setPropName( misDocumentsView.getPropName() );
        documentDTO.setPropNo( misDocumentsView.getPropNo() );
        documentDTO.setPropRemarks( misDocumentsView.getPropRemarks() );
        documentDTO.setPropResponsible( misDocumentsView.getPropResponsible() );
        documentDTO.setPropStatus( misDocumentsView.getPropStatus() );
        documentDTO.setPropType( misDocumentsView.getPropType() );
        documentDTO.setPropWater( misDocumentsView.getPropWater() );
        documentDTO.setTenantAgrType( misDocumentsView.getTenantAgrType() );
        documentDTO.setTenantCompanyName( misDocumentsView.getTenantCompanyName() );
        documentDTO.setTenantCrno( misDocumentsView.getTenantCrno() );
        documentDTO.setTenantDocId( misDocumentsView.getTenantDocId() );
        documentDTO.setTenantExpat( misDocumentsView.getTenantExpat() );
        documentDTO.setTenantFullName( misDocumentsView.getTenantFullName() );
        documentDTO.setTenantIdNo( misDocumentsView.getTenantIdNo() );
        documentDTO.setTenantPhone( misDocumentsView.getTenantPhone() );
        documentDTO.setTenantTele( misDocumentsView.getTenantTele() );

        return documentDTO;
    }

    @Override
    public MisDocuments toEntity(DocumentDTO documentDTO) {
        if ( documentDTO == null ) {
            return null;
        }

        MisDocuments misDocuments = new MisDocuments();

        misDocuments.setDeleted( documentDTO.isDeleted() );
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
    public void updateMisDocumentsFromDto(DocumentDTO documentDTO, MisDocuments misDocuments) {
        if ( documentDTO == null ) {
            return;
        }

        misDocuments.setDeleted( documentDTO.isDeleted() );
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
