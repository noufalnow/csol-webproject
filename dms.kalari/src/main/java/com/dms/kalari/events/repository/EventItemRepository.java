package com.dms.kalari.events.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dms.kalari.common.BaseRepository;
import com.dms.kalari.events.entity.EventItem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventItemRepository extends BaseRepository<EventItem, Long> {

	@Query("SELECT ei FROM EventItem ei WHERE ei.evitemId = :id AND ei.deleted = false")
	Optional<EventItem> findByIdAndNotDeleted(Long id);

	@Query("""
			    SELECT ei FROM EventItem ei
			    WHERE ei.deleted = false
			      AND (:code IS NULL OR :code = '' OR LOWER(ei.evitemCode) LIKE LOWER(CONCAT('%', :code, '%')))
			      AND (:name IS NULL OR :name = '' OR LOWER(ei.evitemName) LIKE LOWER(CONCAT('%', :name, '%')))
			""")
	Page<EventItem> findAllNotDeleted(@Param("code") String code, @Param("name") String name, Pageable pageable);

	@Query("SELECT ei.evitemId, ei.evitemName "
			+ "FROM EventItem ei WHERE ei.deleted = false ORDER BY ei.evitemName ASC")
	List<Object[]> findIdNamePairs();

}
