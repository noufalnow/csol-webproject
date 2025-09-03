package com.dms.kalari.repository;


import org.springframework.stereotype.Repository;
import com.dms.kalari.admin.entity.AuthLogAction;
import com.dms.kalari.common.BaseRepository;

@Repository
public interface AuthLogActionRepository extends BaseRepository<AuthLogAction, Long> { }
