package com.example.tenant_service.service;

import com.example.common.exceptions.ResourceNotFoundException;
import com.example.tenant_service.common.BaseService;
import com.example.tenant_service.dto.DocumentDTO;
import com.example.tenant_service.entity.MisDocuments;
import com.example.tenant_service.mapper.MisDocumentsMapper;
import com.example.tenant_service.repository.MisDocumentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MisDocumentsService implements BaseService<DocumentDTO> {

    private final MisDocumentsRepository misDocumentsRepository;
    private final MisDocumentsMapper misDocumentsMapper;

    @Autowired
    public MisDocumentsService(MisDocumentsRepository misDocumentsRepository, MisDocumentsMapper misDocumentsMapper) {
        this.misDocumentsRepository = misDocumentsRepository;
        this.misDocumentsMapper = misDocumentsMapper;
    }

    @Override
    public DocumentDTO update(Long docId, DocumentDTO DocumentDTO) {
        Optional<MisDocuments> existingDocumentOptional = misDocumentsRepository.findByIdAndNotDeleted(docId);
        if (existingDocumentOptional.isPresent()) {
            MisDocuments existingDocument = existingDocumentOptional.get();

            if (DocumentDTO.getDocType() != null) {
                existingDocument.setDocType(DocumentDTO.getDocType());
            }
            if (DocumentDTO.getDocRefType() != null) {
                existingDocument.setDocRefType(DocumentDTO.getDocRefType());
            }
            if (DocumentDTO.getDocNo() != null) {
                existingDocument.setDocNo(DocumentDTO.getDocNo());
            }
            if (DocumentDTO.getDocDesc() != null) {
                existingDocument.setDocDesc(DocumentDTO.getDocDesc());
            }
            if (DocumentDTO.getDocRemarks() != null) {
                existingDocument.setDocRemarks(DocumentDTO.getDocRemarks());
            }
            if (DocumentDTO.getDocIssueAuth() != null) {
                existingDocument.setDocIssueAuth(DocumentDTO.getDocIssueAuth());
            }
            if (DocumentDTO.getDocApplyDate() != null) {
                existingDocument.setDocApplyDate(DocumentDTO.getDocApplyDate());
            }
            if (DocumentDTO.getDocIssueDate() != null) {
                existingDocument.setDocIssueDate(DocumentDTO.getDocIssueDate());
            }
            if (DocumentDTO.getDocExpiryDate() != null) {
                existingDocument.setDocExpiryDate(DocumentDTO.getDocExpiryDate());
            }
            if (DocumentDTO.getDocAlertDays() != null) {
                existingDocument.setDocAlertDays(DocumentDTO.getDocAlertDays());
            }
            if (DocumentDTO.getDocAmount() != null) {
                existingDocument.setDocAmount(DocumentDTO.getDocAmount());
            }
            if (DocumentDTO.getDocTax() != null) {
                existingDocument.setDocTax(DocumentDTO.getDocTax());
            }
            if (DocumentDTO.getDocPaydet() != null) {
                existingDocument.setDocPaydet(DocumentDTO.getDocPaydet());
            }
            if (DocumentDTO.getDocTntId() != null) {
                existingDocument.setDocTntId(DocumentDTO.getDocTntId());
            }

            existingDocument.setTModified(LocalDateTime.now());
            MisDocuments updatedDocument = misDocumentsRepository.save(existingDocument);
            return misDocumentsMapper.toDTO(updatedDocument);
        } else {
            throw new ResourceNotFoundException("MisDocuments", docId);
        }
    }

    @Override
    public List<DocumentDTO> findAll() {
        return misDocumentsRepository.findAllNotDeleted().stream()
                .map(misDocumentsMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DocumentDTO findById(Long docId) {
        return misDocumentsRepository.findByIdAndNotDeleted(docId)
                .map(misDocumentsMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("MisDocuments", docId));
    }

    @Override
    public DocumentDTO save(DocumentDTO DocumentDTO) {
        MisDocuments misDocuments = misDocumentsMapper.toEntity(DocumentDTO);
        MisDocuments savedDocument = misDocumentsRepository.save(misDocuments);
        return misDocumentsMapper.toDTO(savedDocument);
    }

    @Override
    public void softDeleteById(Long docId) {
        Optional<MisDocuments> documentOptional = misDocumentsRepository.findByIdAndNotDeleted(docId);
        if (documentOptional.isPresent()) {
            MisDocuments document = documentOptional.get();
            document.setDeleted((Boolean) true);
            document.setTDeleted(LocalDateTime.now());
            misDocumentsRepository.save(document);
        } else {
            throw new ResourceNotFoundException("MisDocuments", docId);
        }
    }
}
