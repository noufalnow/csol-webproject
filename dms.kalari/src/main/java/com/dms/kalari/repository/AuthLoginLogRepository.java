package com.dms.kalari.repository;


import org.springframework.stereotype.Repository;
import com.dms.kalari.admin.entity.AuthLoginLog;
import com.dms.kalari.common.BaseRepository;

@Repository
public interface AuthLoginLogRepository extends BaseRepository<AuthLoginLog, Long> { }

