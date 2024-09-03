package com.example.tenant_service.service;

import com.example.common.exceptions.ResourceNotFoundException;
import com.example.tenant_service.common.BaseService;
import com.example.tenant_service.dto.BuildingDTO;
import com.example.tenant_service.entity.MisBuilding;
import com.example.tenant_service.mapper.MisBuildingMapper;
import com.example.tenant_service.repository.MisBuildingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public BuildingDTO update(Long bldId, BuildingDTO BuildingDTO) {
        Optional<MisBuilding> existingBuildingOptional = misBuildingRepository.findByIdAndNotDeleted(bldId);
        if (existingBuildingOptional.isPresent()) {
            MisBuilding existingBuilding = existingBuildingOptional.get();

            if (BuildingDTO.getBldName() != null) {
                existingBuilding.setBldName(BuildingDTO.getBldName());
            }
            if (BuildingDTO.getBldNo() != null) {
                existingBuilding.setBldNo(BuildingDTO.getBldNo());
            }
            if (BuildingDTO.getBldArea() != null) {
                existingBuilding.setBldArea(BuildingDTO.getBldArea());
            }
            if (BuildingDTO.getBldBlockNo() != null) {
                existingBuilding.setBldBlockNo(BuildingDTO.getBldBlockNo());
            }
            if (BuildingDTO.getBldPlotNo() != null) {
                existingBuilding.setBldPlotNo(BuildingDTO.getBldPlotNo());
            }
            if (BuildingDTO.getBldWay() != null) {
                existingBuilding.setBldWay(BuildingDTO.getBldWay());
            }
            if (BuildingDTO.getBldStreet() != null) {
                existingBuilding.setBldStreet(BuildingDTO.getBldStreet());
            }
            if (BuildingDTO.getBldBlock() != null) {
                existingBuilding.setBldBlock(BuildingDTO.getBldBlock());
            }

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
    public BuildingDTO save(BuildingDTO BuildingDTO) {
        MisBuilding misBuilding = misBuildingMapper.toEntity(BuildingDTO);
        MisBuilding savedBuilding = misBuildingRepository.save(misBuilding);
        return misBuildingMapper.toDTO(savedBuilding);
    }

    @Override
    public void softDeleteById(Long bldId) {
        Optional<MisBuilding> buildingOptional = misBuildingRepository.findByIdAndNotDeleted(bldId);
        if (buildingOptional.isPresent()) {
            MisBuilding building = buildingOptional.get();
            building.setDeleted((Boolean) true);
            building.setTDeleted(LocalDateTime.now());
            misBuildingRepository.save(building);
        } else {
            throw new ResourceNotFoundException("MisBuilding", bldId);
        }
    }
}
