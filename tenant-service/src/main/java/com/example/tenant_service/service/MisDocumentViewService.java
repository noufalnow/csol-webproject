package com.example.tenant_service.service;

import com.example.tenant_service.common.BaseService;
import com.example.tenant_service.dto.DocumentsDTO;
import com.example.tenant_service.dto.DocumentsViewDTO;
import com.example.tenant_service.entity.MisDocumentsView;
import com.example.tenant_service.exception.ResourceNotFoundException;
import com.example.tenant_service.mapper.MisDocumentsMapper;
import com.example.tenant_service.mapper.MisDocumentsViewMapper;
import com.example.tenant_service.repository.MisDocumentsRepository;
import com.example.tenant_service.repository.MisDocumentsViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MisDocumentViewService implements BaseService<DocumentsViewDTO> {

    private final MisDocumentsViewRepository misDocumentsViewRepository;
    private final MisDocumentsRepository misDocumentsRepository;
    
    private final MisDocumentsViewMapper misDocumentsViewMapper;
    private final MisDocumentsMapper misDocumentsMapper;

    @Autowired
    public MisDocumentViewService(MisDocumentsViewRepository misDocumentsViewRepository, MisDocumentsViewMapper misDocumentsViewMapper,
    		MisDocumentsRepository misDocumentsRepository, MisDocumentsMapper misDocumentsMapper) {
        this.misDocumentsViewRepository = misDocumentsViewRepository;
        this.misDocumentsViewMapper = misDocumentsViewMapper;
        this.misDocumentsMapper = misDocumentsMapper;
        this.misDocumentsRepository = misDocumentsRepository;
    }

    @Override
    public DocumentsViewDTO findById(Long docId) {
        MisDocumentsView documentView = misDocumentsViewRepository.findByIdAndNotDeleted(docId)
                .orElseThrow(() -> new ResourceNotFoundException("MisDocumentsView", docId));
        return misDocumentsViewMapper.toDTO(documentView);
    }

    @Override
    public List<DocumentsViewDTO> findAll() {
        return misDocumentsViewRepository.findAllNotDeleted().stream()
                .map(misDocumentsViewMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<DocumentsDTO> findAllByDocAgreement(Long docAgreement, Long docId) {
        return misDocumentsRepository.findAllNotDeletedAndByDocAgreement(docAgreement,docId).stream()
                .map(misDocumentsMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<DocumentsViewDTO> findAllPaginate(Pageable pageable, String search) {
        return misDocumentsViewRepository.searchDocuments(search, pageable)
                .map(misDocumentsViewMapper::toDTO);
    }

    @Override
    public DocumentsViewDTO save(DocumentsViewDTO documentsViewDTO) {
        throw new UnsupportedOperationException("This operation is not supported in MisDocumentsViewService");
    }

    @Override
    public DocumentsViewDTO update(Long docId, DocumentsViewDTO documentsViewDTO) {
        throw new UnsupportedOperationException("This operation is not supported in MisDocumentsViewService");
    }

    @Override
    public void softDeleteById(Long docId) {
        throw new UnsupportedOperationException("This operation is not supported in MisDocumentsViewService");
    }
}
