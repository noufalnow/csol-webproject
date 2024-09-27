package com.example.tenant_service.service;

import com.example.tenant_service.common.BaseService;
import com.example.tenant_service.dto.PropertyPayOptionDTO;
import com.example.tenant_service.entity.MisPropertyPayOption;
import com.example.tenant_service.exception.ResourceNotFoundException;
import com.example.tenant_service.mapper.MisPropertyPayOptionMapper;
import com.example.tenant_service.repository.MisPropertyPayOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MisPropertyPayOptionService implements BaseService<PropertyPayOptionDTO> {

    private final MisPropertyPayOptionRepository propertyPayOptionRepository;
    private final MisPropertyPayOptionMapper propertyPayOptionMapper;

    @Autowired
    public MisPropertyPayOptionService(MisPropertyPayOptionRepository propertyPayOptionRepository, MisPropertyPayOptionMapper propertyPayOptionMapper) {
        this.propertyPayOptionRepository = propertyPayOptionRepository;
        this.propertyPayOptionMapper = propertyPayOptionMapper;
    }

    @Override
    public PropertyPayOptionDTO update(Long poptId, PropertyPayOptionDTO dto) {
        Optional<MisPropertyPayOption> existingPayOptionOptional = propertyPayOptionRepository.findById(poptId);
        if (existingPayOptionOptional.isPresent()) {
            MisPropertyPayOption existingPayOption = existingPayOptionOptional.get();
            existingPayOption.setPoptType(dto.getPoptType());
            existingPayOption.setPoptDate(dto.getPoptDate());
            existingPayOption.setPoptAmount(dto.getPoptAmount());
            existingPayOption.setPoptBank(dto.getPoptBank());
            existingPayOption.setPoptChqno(dto.getPoptChqno());
            existingPayOption.setPoptStatus(dto.getPoptStatus());
            existingPayOption.setPoptStatusDate(dto.getPoptStatusDate());

            MisPropertyPayOption updatedPayOption = propertyPayOptionRepository.save(existingPayOption);
            return propertyPayOptionMapper.toDTO(updatedPayOption);
        } else {
            throw new ResourceNotFoundException("MisPropertyPayOption", poptId);
        }
    }

    @Override
    public List<PropertyPayOptionDTO> findAll() {
        return propertyPayOptionRepository.findAllNotDeleted().stream()
                .map(propertyPayOptionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PropertyPayOptionDTO findById(Long poptId) {
        return propertyPayOptionRepository.findById(poptId)
                .map(propertyPayOptionMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("MisPropertyPayOption", poptId));
    }

    @Override
    public PropertyPayOptionDTO save(PropertyPayOptionDTO dto) {
        MisPropertyPayOption payOption = propertyPayOptionMapper.toEntity(dto);
        MisPropertyPayOption savedPayOption = propertyPayOptionRepository.save(payOption);
        return propertyPayOptionMapper.toDTO(savedPayOption);
    }

    @Override
    public void softDeleteById(Long poptId) {
        Optional<MisPropertyPayOption> payOptionOptional = propertyPayOptionRepository.findById(poptId);
        if (payOptionOptional.isPresent()) {
            MisPropertyPayOption payOption = payOptionOptional.get();
            payOption.setDeleted(true);
            payOption.setTDeleted(java.time.LocalDateTime.now());
            propertyPayOptionRepository.save(payOption);
        } else {
            throw new ResourceNotFoundException("MisPropertyPayOption", poptId);
        }
    }

    @Override
    public Page<PropertyPayOptionDTO> findAllPaginate(Pageable pageable, String search) {
        return propertyPayOptionRepository.findAllNotDeleted(search == null ? "" : search, pageable)
                .map(propertyPayOptionMapper::toDTO);
    }
}
