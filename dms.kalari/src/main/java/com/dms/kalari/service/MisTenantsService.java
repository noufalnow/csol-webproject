package com.dms.kalari.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dms.kalari.common.BaseService;
import com.dms.kalari.dto.PropertyDTO;
import com.dms.kalari.dto.TenantDTO;
import com.dms.kalari.entity.MisTenants;
import com.dms.kalari.exception.ResourceNotFoundException;
import com.dms.kalari.mapper.MisTenantsMapper;
import com.dms.kalari.repository.MisTenantsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MisTenantsService implements BaseService<TenantDTO> {

    private final MisTenantsRepository misTenantsRepository;
    private final MisTenantsMapper misTenantsMapper;

    @Autowired
    public MisTenantsService(MisTenantsRepository misTenantsRepository, MisTenantsMapper misTenantsMapper) {
        this.misTenantsRepository = misTenantsRepository;
        this.misTenantsMapper = misTenantsMapper;
    }

    @Override
    public TenantDTO update(Long tntId, TenantDTO TenantDTO) {
        Optional<MisTenants> existingTenantOptional = misTenantsRepository.findByIdAndNotDeleted(tntId);
        if (existingTenantOptional.isPresent()) {
            MisTenants existingTenant = existingTenantOptional.get();

            if (TenantDTO.getTntFullName() != null) {
                existingTenant.setTntFullName(TenantDTO.getTntFullName());
            }
            if (TenantDTO.getTntCompName() != null) {
                existingTenant.setTntCompName(TenantDTO.getTntCompName());
            }
            if (TenantDTO.getTntPhone() != null) {
                existingTenant.setTntPhone(TenantDTO.getTntPhone());
            }
            if (TenantDTO.getTntTele() != null) {
                existingTenant.setTntTele(TenantDTO.getTntTele());
            }
            if (TenantDTO.getTntIdNo() != null) {
                existingTenant.setTntIdNo(TenantDTO.getTntIdNo());
            }
            if (TenantDTO.getTntCrno() != null) {
                existingTenant.setTntCrno(TenantDTO.getTntCrno());
            }
            if (TenantDTO.getTntExpat() != null) {
                existingTenant.setTntExpat(TenantDTO.getTntExpat());
            }
            if (TenantDTO.getTntAgrType() != null) {
                existingTenant.setTntAgrType(TenantDTO.getTntAgrType());
            }
            if (TenantDTO.getTntDocId() != null) {
                existingTenant.setTntDocId(TenantDTO.getTntDocId());
            }

            existingTenant.setTModified(LocalDateTime.now());
            MisTenants updatedTenant = misTenantsRepository.save(existingTenant);
            return misTenantsMapper.toDTO(updatedTenant);
        } else {
            throw new ResourceNotFoundException("MisTenants", tntId);
        }
    }

    @Override
    public List<TenantDTO> findAll() {
        return misTenantsRepository.findAllNotDeleted().stream()
                .map(misTenantsMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TenantDTO findById(Long tntId) {
        return misTenantsRepository.findByIdAndNotDeleted(tntId)
                .map(misTenantsMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("MisTenants", tntId));
    }

    @Override
    public TenantDTO save(TenantDTO TenantDTO) {
        MisTenants misTenants = misTenantsMapper.toEntity(TenantDTO);
        MisTenants savedTenant = misTenantsRepository.save(misTenants);
        return misTenantsMapper.toDTO(savedTenant);
    }

    @Override
    public void softDeleteById(Long tntId) {
        Optional<MisTenants> tenantOptional = misTenantsRepository.findByIdAndNotDeleted(tntId);
        if (tenantOptional.isPresent()) {
            MisTenants tenant = tenantOptional.get();
            tenant.setDeleted((Boolean) true);
            tenant.setTDeleted(LocalDateTime.now());
            misTenantsRepository.save(tenant);
        } else {
            throw new ResourceNotFoundException("MisTenants", tntId);
        }
    }
    
    @Override
    public Page<TenantDTO> findAllPaginate(Pageable pageable, String search) {
        // Assuming findAllPaginate method exists in the repository
        return misTenantsRepository.findAllNotDeleted(search == null ? "" : search, pageable)
                .map(misTenantsMapper::toDTO);
    }
}
