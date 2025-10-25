package com.dms.kalari.core.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import com.dms.kalari.core.entity.CoreFile;
import com.dms.kalari.common.BaseRepository;

@Repository
public interface CoreFileRepository extends BaseRepository<CoreFile, Long> {

    @Query("SELECT f FROM CoreFile f WHERE f.fileId = :fileId AND f.deleted = false")
    Optional<CoreFile> findByIdAndNotDeleted(Long fileId);

    // Find all files that are not deleted
    List<CoreFile> findAllByDeletedFalse();

    // Find by reference (example: all files belonging to a user/branch/event)
    List<CoreFile> findByDeletedFalseAndFileRefId(Long fileRefId);

    // Find by source type (users, branch, event...)
    List<CoreFile> findByDeletedFalseAndFileSrc(String fileSrc);
    
    
    List<CoreFile> findByFileRefIdAndFileSrc(Long fileRefId, String fileSrc);
}

