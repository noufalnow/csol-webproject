package com.example.tenant_service.service;

import com.example.tenant_service.common.BaseService;
import com.example.tenant_service.dto.DocumentsDTO;
import com.example.tenant_service.entity.MisDocuments;
import com.example.tenant_service.entity.MisDocumentsView;
import com.example.tenant_service.exception.ResourceNotFoundException;
import com.example.tenant_service.mapper.MisDocumentsMapper;
import com.example.tenant_service.repository.MisDocumentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MisDocumentDataService implements BaseService<DocumentsDTO> {

    private final MisDocumentsRepository misDocumentsRepository;
    private final MisDocumentsMapper misDocumentsMapper;

    @Autowired
    public MisDocumentDataService(MisDocumentsRepository misDocumentsRepository, MisDocumentsMapper misDocumentsMapper) {
        this.misDocumentsRepository = misDocumentsRepository;
        this.misDocumentsMapper = misDocumentsMapper;
    }

    @Override
    public DocumentsDTO save(DocumentsDTO documentsDTO) {
        MisDocuments misDocuments = misDocumentsMapper.toEntity(documentsDTO);
        misDocuments.setTCreated(LocalDateTime.now());
        MisDocuments savedDocument = misDocumentsRepository.save(misDocuments);
        return misDocumentsMapper.toDTO(savedDocument);
    }

    @Override
    public DocumentsDTO update(Long docId, DocumentsDTO documentsDTO) {
        MisDocuments existingDocument = misDocumentsRepository.findByIdAndNotDeleted(docId)
                .orElseThrow(() -> new ResourceNotFoundException("MisDocuments", docId));

        misDocumentsMapper.updateMisDocumentsFromDto(documentsDTO, existingDocument);
        existingDocument.setTModified(LocalDateTime.now());

        MisDocuments updatedDocument = misDocumentsRepository.save(existingDocument);
        return misDocumentsMapper.toDTO(updatedDocument);
    }

    @Override
    public void softDeleteById(Long docId) {
        MisDocuments existingDocument = misDocumentsRepository.findByIdAndNotDeleted(docId)
                .orElseThrow(() -> new ResourceNotFoundException("MisDocuments", docId));

        existingDocument.setDeleted(true);
        existingDocument.setTDeleted(LocalDateTime.now());
        misDocumentsRepository.save(existingDocument);
    }

    @Override
    public List<DocumentsDTO> findAll() {
        throw new UnsupportedOperationException("This operation is not supported in MisDocumentDataService");
    }

    @Override
    public DocumentsDTO findById(Long docId) {
        //throw new UnsupportedOperationException("This operation is not supported in MisDocumentDataService");
    	

        MisDocuments documentView = misDocumentsRepository.findByIdAndNotDeleted(docId)
                .orElseThrow(() -> new ResourceNotFoundException("MisDocuments", docId));
        return misDocumentsMapper.toDTO(documentView);
    
    	
    }

    @Override
    public Page<DocumentsDTO> findAllPaginate(Pageable pageable, String search) {
        throw new UnsupportedOperationException("This operation is not supported in MisDocumentDataService");
    }
    
    
}
