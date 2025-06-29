package com.example.tenant_service.service;

import com.example.tenant_service.common.BaseService;
import com.example.tenant_service.dto.DesignationDTO;
import com.example.tenant_service.dto.DesignationDTO;
import com.example.tenant_service.entity.MisDesignation;
import com.example.tenant_service.exception.ResourceNotFoundException;
import com.example.tenant_service.mapper.MisDesignationMapper;
import com.example.tenant_service.repository.MisDesignationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    public DesignationDTO update(Long desigId, DesignationDTO designationDTO) {
        Optional<MisDesignation> existingDesignationOptional = misDesignationRepository.findById(desigId);
        if (existingDesignationOptional.isPresent()) {
            MisDesignation existingDesignation = existingDesignationOptional.get();
            existingDesignation.setDesigCode(designationDTO.getDesigCode());
            existingDesignation.setDesigName(designationDTO.getDesigName());
            existingDesignation.setDesigLevel(designationDTO.getDesigLevel());
            existingDesignation.setDesigType(designationDTO.getDesigType());

            MisDesignation updatedDesignation = misDesignationRepository.save(existingDesignation);
            return misDesignationMapper.toDTO(updatedDesignation);
        } else {
            throw new ResourceNotFoundException("MisDesignation", desigId);
        }
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
