package com.example.tenant_service.repository;

import com.example.tenant_service.entity.MisCollectionDet;
import com.example.tenant_service.common.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MisCollectionDetRepository extends BaseRepository<MisCollectionDet, Long> {

    // Additional custom queries can be defined here if required
}
