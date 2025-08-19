package com.dms.kalari.common;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BaseService<DTO> {
    List<DTO> findAll();
    DTO findById(Long id);
    DTO save(DTO dto);
    DTO update(Long id, DTO dto);
    void softDeleteById(Long id);

    // New method to support pagination with search functionality
    Page<DTO> findAllPaginate(Pageable pageable, String search);
}
