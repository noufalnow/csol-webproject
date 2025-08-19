package com.dms.kalari.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dms.kalari.common.BaseService;
import com.dms.kalari.dto.DocumentsDTO;
import com.dms.kalari.dto.DocumentsViewDTO;
import com.dms.kalari.dto.PropertyPayOptionDTO;
import com.dms.kalari.entity.MisDocumentsView;
import com.dms.kalari.entity.MisPropertyPayOption;
import com.dms.kalari.exception.ResourceNotFoundException;
import com.dms.kalari.mapper.MisDocumentsMapper;
import com.dms.kalari.mapper.MisDocumentsViewMapper;
import com.dms.kalari.repository.MisDocumentsRepository;
import com.dms.kalari.repository.MisDocumentsViewRepository;

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
    
    public List<DocumentsViewDTO> findByTenantId(Long docTntId) {
        List<MisDocumentsView> documentView = misDocumentsViewRepository.findByDocTntIdAndNotDeleted(docTntId);
        return documentView.stream()
                .map(misDocumentsViewMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    
    
    
    public List<DocumentsViewDTO> findByTenantIdPendingPayments(Long docTntId) {
        List<MisDocumentsView> documentView = misDocumentsViewRepository.findByDocTntIdAndPendingPayments(docTntId);
        return documentView.stream()
                .map(misDocumentsViewMapper::toDTO)
                .collect(Collectors.toList());
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
