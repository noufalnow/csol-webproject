package com.example.tenant_service.repository;

import com.example.tenant_service.common.BaseRepository;
import com.example.tenant_service.entity.CoreUser;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CoreUserRepository extends BaseRepository<CoreUser, Long> {

    @Query("SELECT u FROM CoreUser u WHERE u.id = :userId AND u.deleted = false")
    Optional<CoreUser> findByIdAndNotDeleted(Long userId);

    @Query("SELECT u FROM CoreUser u WHERE u.deleted = false")
    List<CoreUser> findAllNotDeleted();
}


