package com.example.tenant_service.service;

import com.example.tenant_service.exception.ResourceNotFoundException;
import com.example.tenant_service.common.BaseService;
import com.example.tenant_service.dto.DocumentDTO;
import com.example.tenant_service.dto.PropertyDTO;
import com.example.tenant_service.entity.MisProperty;
import com.example.tenant_service.mapper.MisPropertyMapper;
import com.example.tenant_service.repository.MisPropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MisPropertyService implements BaseService<PropertyDTO> {

    private final MisPropertyRepository misPropertyRepository;
    private final MisPropertyMapper misPropertyMapper;

    @Autowired
    public MisPropertyService(MisPropertyRepository misPropertyRepository, MisPropertyMapper misPropertyMapper) {
        this.misPropertyRepository = misPropertyRepository;
        this.misPropertyMapper = misPropertyMapper;
    }

    @Override
    public PropertyDTO update(Long prtyId, PropertyDTO PropertyDTO) {
        Optional<MisProperty> existingPropertyOptional = misPropertyRepository.findByIdAndNotDeleted(prtyId);
        if (existingPropertyOptional.isPresent()) {
            MisProperty existingProperty = existingPropertyOptional.get();

            if (PropertyDTO.getPropNo() != null) {
                existingProperty.setPropNo(PropertyDTO.getPropNo());
            }
            if (PropertyDTO.getPropFileno() != null) {
                existingProperty.setPropFileno(PropertyDTO.getPropFileno());
            }
            if (PropertyDTO.getPropName() != null) {
                existingProperty.setPropName(PropertyDTO.getPropName());
            }
            if (PropertyDTO.getPropBuilding() != null) {
                existingProperty.setPropBuilding(PropertyDTO.getPropBuilding());
            }
            if (PropertyDTO.getPropResponsible() != null) {
                existingProperty.setPropResponsible(PropertyDTO.getPropResponsible());
            }
            if (PropertyDTO.getPropRemarks() != null) {
                existingProperty.setPropRemarks(PropertyDTO.getPropRemarks());
            }
            if (PropertyDTO.getPropCat() != null) {
                existingProperty.setPropCat(PropertyDTO.getPropCat());
            }
            if (PropertyDTO.getPropType() != null) {
                existingProperty.setPropType(PropertyDTO.getPropType());
            }
            if (PropertyDTO.getPropLevel() != null) {
                existingProperty.setPropLevel(PropertyDTO.getPropLevel());
            }
            if (PropertyDTO.getPropElecMeter() != null) {
                existingProperty.setPropElecMeter(PropertyDTO.getPropElecMeter());
            }
            if (PropertyDTO.getPropWater() != null) {
                existingProperty.setPropWater(PropertyDTO.getPropWater());
            }
            if (PropertyDTO.getPropBuildingType() != null) {
                existingProperty.setPropBuildingType(PropertyDTO.getPropBuildingType());
            }
            if (PropertyDTO.getPropStatus() != null) {
                existingProperty.setPropStatus(PropertyDTO.getPropStatus());
            }
            if (PropertyDTO.getPropElecAccount() != null) {
                existingProperty.setPropElecAccount(PropertyDTO.getPropElecAccount());
            }
            if (PropertyDTO.getPropElecRecharge() != null) {
                existingProperty.setPropElecRecharge(PropertyDTO.getPropElecRecharge());
            }
            if (PropertyDTO.getPropAccount() != null) {
                existingProperty.setPropAccount(PropertyDTO.getPropAccount());
            }


            existingProperty.setTModified(LocalDateTime.now());
            MisProperty updatedProperty = misPropertyRepository.save(existingProperty);
            return misPropertyMapper.toDTO(updatedProperty);
        } else {
            throw new ResourceNotFoundException("MisProperty", prtyId);
        }
    }

    @Override
    public List<PropertyDTO> findAll() {
        return misPropertyRepository.findAllNotDeleted().stream()
                .map(misPropertyMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PropertyDTO findById(Long prtyId) {
        return misPropertyRepository.findByIdAndNotDeleted(prtyId)
                .map(misPropertyMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("MisProperty", prtyId));
    }

    @Override
    public PropertyDTO save(PropertyDTO PropertyDTO) {
        MisProperty misProperty = misPropertyMapper.toEntity(PropertyDTO);
        MisProperty savedProperty = misPropertyRepository.save(misProperty);
        return misPropertyMapper.toDTO(savedProperty);
    }

    @Override
    public void softDeleteById(Long prtyId) {
        Optional<MisProperty> propertyOptional = misPropertyRepository.findByIdAndNotDeleted(prtyId);
        if (propertyOptional.isPresent()) {
            MisProperty property = propertyOptional.get();
            property.setDeleted((Boolean) true);
            property.setTDeleted(LocalDateTime.now());
            misPropertyRepository.save(property);
        } else {
            throw new ResourceNotFoundException("MisProperty", prtyId);
        }
    }
    
    @Override
    public Page<PropertyDTO> findAllPaginate(Pageable pageable, String search) {
        // Assuming findAllPaginate method exists in the repository
        return misPropertyRepository.findAllNotDeleted(search == null ? "" : search, pageable)
                .map(misPropertyMapper::toDTO);
    }
}
