package com.dms.kalari.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dms.kalari.common.BaseService;
import com.dms.kalari.dto.PropertyPayOptionDTO;
import com.dms.kalari.entity.MisPropertyPayOption;
import com.dms.kalari.exception.ResourceNotFoundException;
import com.dms.kalari.mapper.MisPropertyPayOptionMapper;
import com.dms.kalari.repository.MisPropertyPayOptionRepository;

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
    
    
    public List<PropertyPayOptionDTO> findByPoptDocId(Long poptDocId) {
        List<MisPropertyPayOption> payOptions = propertyPayOptionRepository.findByPoptDocIdAndNotDeleted(poptDocId);
        return payOptions.stream()
                .map(propertyPayOptionMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    
    public List<PropertyPayOptionDTO> findByPoptDocIdAndPending(Long poptDocId) {
        List<MisPropertyPayOption> payOptions = propertyPayOptionRepository.findByPoptDocIdAndNotDeletedAndNotPaid(poptDocId);
        return payOptions.stream()
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
