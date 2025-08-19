package com.dms.kalari.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dms.kalari.common.BaseService;
import com.dms.kalari.dto.CollectionDetDTO;
import com.dms.kalari.dto.MisCollectionDTO;
import com.dms.kalari.entity.MisCollectionDet;
import com.dms.kalari.exception.ResourceNotFoundException;
import com.dms.kalari.mapper.MisCollectionDetMapper;
import com.dms.kalari.repository.MisCollectionDetRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MisCollectionDetService implements BaseService<CollectionDetDTO> {

    private final MisCollectionDetRepository misCollectionDetRepository;
    private final MisCollectionDetMapper misCollectionDetMapper;

    @Autowired
    public MisCollectionDetService(MisCollectionDetRepository misCollectionDetRepository, MisCollectionDetMapper misCollectionDetMapper) {
        this.misCollectionDetRepository = misCollectionDetRepository;
        this.misCollectionDetMapper = misCollectionDetMapper;
    }

    @Override
    public CollectionDetDTO update(Long cdetId, CollectionDetDTO collectionDetDTO) {
        Optional<MisCollectionDet> existingCollectionDetOptional = misCollectionDetRepository.findById(cdetId);
        if (existingCollectionDetOptional.isPresent()) {
            MisCollectionDet existingCollectionDet = existingCollectionDetOptional.get();
            existingCollectionDet.setCdetCollId(collectionDetDTO.getCdetCollId());
            existingCollectionDet.setCdetPoptId(collectionDetDTO.getCdetPoptId());
            existingCollectionDet.setCdetAmtToPay(collectionDetDTO.getCdetAmtToPay());
            existingCollectionDet.setCdetAmtPaid(collectionDetDTO.getCdetAmtPaid());

            MisCollectionDet updatedCollectionDet = misCollectionDetRepository.save(existingCollectionDet);
            return misCollectionDetMapper.toDTO(updatedCollectionDet);
        } else {
            throw new ResourceNotFoundException("MisCollectionDet", cdetId);
        }
    }

    @Override
    public List<CollectionDetDTO> findAll() {
        return misCollectionDetRepository.findAll().stream()
                .map(misCollectionDetMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CollectionDetDTO findById(Long cdetId) {
        return misCollectionDetRepository.findById(cdetId)
                .map(misCollectionDetMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("MisCollectionDet", cdetId));
    }

    @Override
    public CollectionDetDTO save(CollectionDetDTO collectionDetDTO) {
        MisCollectionDet misCollectionDet = misCollectionDetMapper.toEntity(collectionDetDTO);
        MisCollectionDet savedCollectionDet = misCollectionDetRepository.save(misCollectionDet);
        return misCollectionDetMapper.toDTO(savedCollectionDet);
    }

    @Override
    public void softDeleteById(Long cdetId) {
        Optional<MisCollectionDet> collectionDetOptional = misCollectionDetRepository.findById(cdetId);
        if (collectionDetOptional.isPresent()) {
            MisCollectionDet collectionDet = collectionDetOptional.get();
            collectionDet.setDeleted(true);
            collectionDet.setTDeleted(java.time.LocalDateTime.now());
            misCollectionDetRepository.save(collectionDet);
        } else {
            throw new ResourceNotFoundException("MisCollectionDet", cdetId);
        }
    }
    
    @Override
    public Page<CollectionDetDTO> findAllPaginate(Pageable pageable, String search) {
        // Add implementation for paginated search
        return null; // Example placeholder
    }
}
