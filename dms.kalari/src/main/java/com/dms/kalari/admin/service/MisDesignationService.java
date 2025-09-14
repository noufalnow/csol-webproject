package com.dms.kalari.admin.service;

import org.hibernate.validator.internal.util.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dms.kalari.admin.dto.DesignationDTO;
import com.dms.kalari.admin.entity.MisDesignation;
import com.dms.kalari.admin.mapper.MisDesignationMapper;
import com.dms.kalari.admin.repository.MisDesignationRepository;
import com.dms.kalari.branch.entity.Node;
import com.dms.kalari.common.BaseService;
import com.dms.kalari.exception.ResourceNotFoundException;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MisDesignationService implements BaseService<DesignationDTO> {

    private final MisDesignationRepository misDesignationRepository;
    private final MisDesignationMapper misDesignationMapper;

    @Autowired
    public MisDesignationService(MisDesignationRepository misDesignationRepository, MisDesignationMapper misDesignationMapper) {
        this.misDesignationRepository = misDesignationRepository;
        this.misDesignationMapper = misDesignationMapper;
    }

    @Override
    @Transactional
    public DesignationDTO update(Long desigId, DesignationDTO designationDTO) {
        MisDesignation existingDesignation = misDesignationRepository.findById(desigId)
            .orElseThrow(() -> new ResourceNotFoundException("MisDesignation", desigId));

        existingDesignation.setDesigCode(designationDTO.getDesigCode());
        existingDesignation.setDesigName(designationDTO.getDesigName());
        existingDesignation.setDesigLevel(designationDTO.getDesigLevel());
        //existingDesignation.setDesigLevel(designationDTO.getDesigLevel().name());
        existingDesignation.setDesigType(designationDTO.getDesigType());
        existingDesignation.setTModified(LocalDateTime.now());  // <-- add this

        MisDesignation updatedDesignation = misDesignationRepository.save(existingDesignation);
        misDesignationRepository.flush();  // force SQL execution

        // Print or log the updated entity
        System.out.println("Updated Designation: " + updatedDesignation);

        return misDesignationMapper.toDTO(updatedDesignation);

    }

    // Fetch all designations that are not deleted
    @Override
    public List<DesignationDTO> findAll() {
        return misDesignationRepository.findAllByDeletedFalse().stream()
                .map(misDesignationMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<DesignationDTO> findAllByType(Short type) {
        return misDesignationRepository.findByDeletedFalseAndDesigType(type).stream()
                .map(misDesignationMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    
    public List<DesignationDTO> findAllByDesigNodeLevel(Node.Type type) {
        return misDesignationRepository.findByDeletedFalseAndDesigLevel(type).stream()
                .map(misDesignationMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<DesignationDTO> findAllByDesigNodeLevelAndType(Node.Type type,Short desigType) {
        return misDesignationRepository.findByDeletedFalseAndDesigLevelAndDesigType(type, desigType).stream()
                .map(misDesignationMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    
    

    @Override
    public DesignationDTO findById(Long desigId) {
        return misDesignationRepository.findById(desigId)
                .map(misDesignationMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("MisDesignation", desigId));
    }

    @Override
    public DesignationDTO save(DesignationDTO designationDTO) {
        MisDesignation misDesignation = misDesignationMapper.toEntity(designationDTO);
        MisDesignation savedDesignation = misDesignationRepository.save(misDesignation);
        return misDesignationMapper.toDTO(savedDesignation);
    }

    @Override
    public void softDeleteById(Long desigId) {
        Optional<MisDesignation> designationOptional = misDesignationRepository.findById(desigId);
        if (designationOptional.isPresent()) {
            MisDesignation designation = designationOptional.get();
            designation.setDeleted(true);
            designation.setTDeleted(java.time.LocalDateTime.now());
            misDesignationRepository.save(designation);
        } else {
            throw new ResourceNotFoundException("MisDesignation", desigId);
        }
    }
    
    // Fetch paginated results of designations that are not deleted and allow search functionality
    @Override
    public Page<DesignationDTO> findAllPaginate(Pageable pageable, String search) {
        return misDesignationRepository.findAllNotDeleted(search == null ? "" : search, pageable)
                .map(misDesignationMapper::toDTO);
    }
}
