package com.example.tenant_service.service;

import com.example.common.exceptions.ResourceNotFoundException;
import com.example.tenant_service.common.BaseService;
import com.example.tenant_service.dto.PropertyStatusDTO;
import com.example.tenant_service.entity.MisPropertyStatus;
import com.example.tenant_service.mapper.MisPropertyStatusMapper;
import com.example.tenant_service.repository.MisPropertyStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MisPropertyStatusService implements BaseService<PropertyStatusDTO> {

    private final MisPropertyStatusRepository misPropertyStatusRepository;
    private final MisPropertyStatusMapper misPropertyStatusMapper;

    @Autowired
    public MisPropertyStatusService(MisPropertyStatusRepository misPropertyStatusRepository, MisPropertyStatusMapper misPropertyStatusMapper) {
        this.misPropertyStatusRepository = misPropertyStatusRepository;
        this.misPropertyStatusMapper = misPropertyStatusMapper;
    }

    @Override
    public PropertyStatusDTO update(Long prtyStatusId, PropertyStatusDTO PropertyStatusDTO) {
        Optional<MisPropertyStatus> existingPropertyStatusOptional = misPropertyStatusRepository.findByIdAndNotDeleted(prtyStatusId);
        if (existingPropertyStatusOptional.isPresent()) {
            MisPropertyStatus existingPropertyStatus = existingPropertyStatusOptional.get();

            if (PropertyStatusDTO.getPstsType() != null) {
                existingPropertyStatus.setPstsType(PropertyStatusDTO.getPstsType());
            }
            if (PropertyStatusDTO.getPstsRemarks() != null) {
                existingPropertyStatus.setPstsRemarks(PropertyStatusDTO.getPstsRemarks());
            }
            if (PropertyStatusDTO.getPstsStatusDate() != null) {
                existingPropertyStatus.setPstsStatusDate(PropertyStatusDTO.getPstsStatusDate());
            }
            if (PropertyStatusDTO.getPstsAttachProp() != null) {
                existingPropertyStatus.setPstsAttachProp(PropertyStatusDTO.getPstsAttachProp());
            }


            existingPropertyStatus.setTModified(LocalDateTime.now());
            MisPropertyStatus updatedPropertyStatus = misPropertyStatusRepository.save(existingPropertyStatus);
            return misPropertyStatusMapper.toDTO(updatedPropertyStatus);
        } else {
            throw new ResourceNotFoundException("MisPropertyStatus", prtyStatusId);
        }
    }

    @Override
    public List<PropertyStatusDTO> findAll() {
        return misPropertyStatusRepository.findAllNotDeleted().stream()
                .map(misPropertyStatusMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PropertyStatusDTO findById(Long prtyStatusId) {
        return misPropertyStatusRepository.findByIdAndNotDeleted(prtyStatusId)
                .map(misPropertyStatusMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("MisPropertyStatus", prtyStatusId));
    }

    @Override
    public PropertyStatusDTO save(PropertyStatusDTO PropertyStatusDTO) {
        MisPropertyStatus misPropertyStatus = misPropertyStatusMapper.toEntity(PropertyStatusDTO);
        MisPropertyStatus savedPropertyStatus = misPropertyStatusRepository.save(misPropertyStatus);
        return misPropertyStatusMapper.toDTO(savedPropertyStatus);
    }

    @Override
    public void softDeleteById(Long prtyStatusId) {
        Optional<MisPropertyStatus> propertyStatusOptional = misPropertyStatusRepository.findByIdAndNotDeleted(prtyStatusId);
        if (propertyStatusOptional.isPresent()) {
            MisPropertyStatus propertyStatus = propertyStatusOptional.get();
            propertyStatus.setDeleted((Boolean) true);
            propertyStatus.setTDeleted(LocalDateTime.now());
            misPropertyStatusRepository.save(propertyStatus);
        } else {
            throw new ResourceNotFoundException("MisPropertyStatus", prtyStatusId);
        }
    }
}
