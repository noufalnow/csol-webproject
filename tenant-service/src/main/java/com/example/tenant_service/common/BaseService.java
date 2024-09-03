package com.example.tenant_service.common;

import java.util.List;

public interface BaseService<DTO> {
    List<DTO> findAll();
    DTO findById(Long id);
    DTO save(DTO dto);
    DTO update(Long id, DTO dto);
    void softDeleteById(Long id);
}
