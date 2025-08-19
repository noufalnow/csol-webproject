package com.dms.kalari.repository;

import org.springframework.stereotype.Repository;

import com.dms.kalari.common.BaseRepository;
import com.dms.kalari.entity.MisCollectionDet;

@Repository
public interface MisCollectionDetRepository extends BaseRepository<MisCollectionDet, Long> {

    // Additional custom queries can be defined here if required
}
