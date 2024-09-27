package com.example.tenant_service.repository;

import com.example.tenant_service.entity.MisPropertyPayOption;
import com.example.tenant_service.common.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MisPropertyPayOptionRepository extends BaseRepository<MisPropertyPayOption, Long> {

    // Find all records that are not deleted with search criteria and pagination
    @Query("SELECT p FROM MisPropertyPayOption p WHERE p.deleted = false AND " +
            "(CAST(p.poptId AS string) LIKE %:search% OR " +
            "CAST(p.poptAmount AS string) LIKE %:search% OR " +
            "CAST(p.poptType AS string) LIKE %:search%)")
    Page<MisPropertyPayOption> findAllNotDeleted(String search, Pageable pageable);
}
