package com.dms.kalari.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dms.kalari.common.BaseService;
import com.dms.kalari.dto.BuildingDTO;
import com.dms.kalari.entity.MisBuilding;
import com.dms.kalari.exception.ResourceNotFoundException;
import com.dms.kalari.mapper.MisBuildingMapper;
import com.dms.kalari.repository.MisBuildingRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MisBuildingService implements BaseService<BuildingDTO> {

    private final MisBuildingRepository misBuildingRepository;
    private final MisBuildingMapper misBuildingMapper;

    @Autowired
    public MisBuildingService(MisBuildingRepository misBuildingRepository, MisBuildingMapper misBuildingMapper) {
        this.misBuildingRepository = misBuildingRepository;
        this.misBuildingMapper = misBuildingMapper;
    }

    @Override
    public BuildingDTO update(Long bldId, BuildingDTO buildingDTO) {
        Optional<MisBuilding> existingBuildingOptional = misBuildingRepository.findByIdAndNotDeleted(bldId);
        if (existingBuildingOptional.isPresent()) {
            MisBuilding existingBuilding = existingBuildingOptional.get();

            // Update fields if they are present in the DTO
            existingBuilding.setBldName(buildingDTO.getBldName());
            existingBuilding.setBldNo(buildingDTO.getBldNo());
            existingBuilding.setBldArea(buildingDTO.getBldArea());
            existingBuilding.setBldBlockNo(buildingDTO.getBldBlockNo());
            existingBuilding.setBldPlotNo(buildingDTO.getBldPlotNo());
            existingBuilding.setBldWay(buildingDTO.getBldWay());
            existingBuilding.setBldStreet(buildingDTO.getBldStreet());
            existingBuilding.setBldBlock(buildingDTO.getBldBlock());

            existingBuilding.setTModified(LocalDateTime.now());

            MisBuilding updatedBuilding = misBuildingRepository.save(existingBuilding);
            return misBuildingMapper.toDTO(updatedBuilding);
        } else {
            throw new ResourceNotFoundException("MisBuilding", bldId);
        }
    }

    @Override
    public List<BuildingDTO> findAll() {
        return misBuildingRepository.findAllNotDeleted().stream()
                .map(misBuildingMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BuildingDTO findById(Long bldId) {
        return misBuildingRepository.findByIdAndNotDeleted(bldId)
                .map(misBuildingMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("MisBuilding", bldId));
    }

    @Override
    public BuildingDTO save(BuildingDTO buildingDTO) {
        MisBuilding misBuilding = misBuildingMapper.toEntity(buildingDTO);
        MisBuilding savedBuilding = misBuildingRepository.save(misBuilding);
        return misBuildingMapper.toDTO(savedBuilding);
    }

    @Override
    public void softDeleteById(Long bldId) {
        Optional<MisBuilding> buildingOptional = misBuildingRepository.findByIdAndNotDeleted(bldId);
        if (buildingOptional.isPresent()) {
            MisBuilding building = buildingOptional.get();
            building.setDeleted(true);
            building.setTDeleted(LocalDateTime.now());
            misBuildingRepository.save(building);
        } else {
            throw new ResourceNotFoundException("MisBuilding", bldId);
        }
    }

    @Override
    public Page<BuildingDTO> findAllPaginate(Pageable pageable, String search) {
        return misBuildingRepository.findAllNotDeleted(search == null ? "" : search, pageable)
                .map(misBuildingMapper::toDTO);
    }
}
