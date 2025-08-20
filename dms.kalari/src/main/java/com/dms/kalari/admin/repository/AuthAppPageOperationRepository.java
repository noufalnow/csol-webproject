package com.dms.kalari.admin.repository;

import com.dms.kalari.admin.dto.AppPageOperationDTO;
import com.dms.kalari.admin.entity.AuthAppPageOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AuthAppPageOperationRepository extends JpaRepository<AuthAppPageOperation, Long> {

    @Query("SELECT new com.dms.kalari.admin.dto.AppPageOperationDTO(" +
           "o.operationId, p.appPageId, o.alias, o.operation, o.realPath) " +
           "FROM AuthAppPageOperation o " +
           "JOIN o.appPage p " +
           "WHERE o.alias = :alias AND o.operation = :method")
    Optional<AppPageOperationDTO> findActiveByAliasAndMethod(@Param("alias") String alias,
                                                            @Param("method") String method);
}
