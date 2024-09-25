package com.example.tenant_service.service;

import com.example.tenant_service.exception.ResourceNotFoundException;
import com.example.tenant_service.common.BaseService;
import com.example.tenant_service.dto.DocumentDTO;
import com.example.tenant_service.mapper.MisDocumentsMapper;
import com.example.tenant_service.repository.MisDocumentsViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MisDocumentsService implements BaseService<DocumentDTO> {

    private final MisDocumentsViewRepository misDocumentsViewRepository;
    private final MisDocumentsMapper misDocumentsMapper;

    @Autowired
    public MisDocumentsService(MisDocumentsViewRepository misDocumentsViewRepository,
                               MisDocumentsMapper misDocumentsMapper) {
        this.misDocumentsViewRepository = misDocumentsViewRepository;
        this.misDocumentsMapper = misDocumentsMapper;
    }

    @Override
    public DocumentDTO update(Long docId, DocumentDTO documentDTO) {
        // Assuming this should throw an exception as updates are not supported for views
        throw new UnsupportedOperationException("Update operation is not supported on views.");
    }

    @Override
    public List<DocumentDTO> findAll() {
        return misDocumentsViewRepository.findAll()
                .stream()
                .filter(view -> !view.isDeleted()) // Ensure not deleted
                .map(misDocumentsMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DocumentDTO findById(Long docId) {
        return misDocumentsViewRepository.findByIdAndNotDeleted(docId)
                .map(misDocumentsMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("MisDocuments", docId));
    }

    @Override
    public DocumentDTO save(DocumentDTO documentDTO) {
        // Assuming this should throw an exception as save is not supported for views
        throw new UnsupportedOperationException("Save operation is not supported on views.");
    }

    @Override
    public void softDeleteById(Long docId) {
        // Assuming this should throw an exception as soft delete is not supported for views
        throw new UnsupportedOperationException("Soft delete operation is not supported on views.");
    }

    @Override
    public Page<DocumentDTO> findAllPaginate(Pageable pageable, String search) {
        return misDocumentsViewRepository.searchDocuments(search, pageable)
                .map(misDocumentsMapper::toDTO);
    }
}
