package com.dms.kalari.events.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dms.kalari.admin.entity.CoreUser;
import com.dms.kalari.events.dto.EventChestConfigDTO;
import com.dms.kalari.events.entity.EventChestConfig;
import com.dms.kalari.events.entity.EventItemMap;

import jakarta.transaction.Transactional;

@Repository
public interface EventChestConfigRepository
        extends JpaRepository<EventChestConfig, Long> {

    /*
     * Find by EventItemMap + Gender
     */
    Optional<EventChestConfig> findByEventItemMapAndGender(
            EventItemMap eventItemMap,
            CoreUser.Gender gender
    );

    /*
     * Find by eimId + gender
     */
    @Query("""
        SELECT ecc
        FROM EventChestConfig ecc
        WHERE ecc.eventItemMap.eimId = :eimId
        AND ecc.gender = :gender
        AND ecc.deleted = false
    """)
    Optional<EventChestConfig> findByEimIdAndGender(
            Long eimId,
            CoreUser.Gender gender
    );

    /*
     * List all configs by event
     */
    @Query("""
        SELECT ecc
        FROM EventChestConfig ecc
        WHERE ecc.eventItemMap.event.eventId = :eventId
        AND ecc.deleted = false
        ORDER BY ecc.eventItemMap.category,
                 ecc.gender
    """)
    List<EventChestConfig> findByEventId(Long eventId);

    /*
     * DTO projection
     */
    @Query("""
	    SELECT new com.dms.kalari.events.dto.EventChestConfigDTO(
	        ecc.chestConfigId,
	        ecc.eventItemMap.eimId,
	        ecc.eventItemMap.event.eventId,
	        ecc.eventItemMap.item.evitemId,
	        ecc.eventItemMap.item.evitemName,
	        ecc.eventItemMap.category,
	        ecc.gender,
	        ecc.startNo,
	        ecc.currentNo,
	        ecc.prefix,
	        ecc.suffix
	    )
	    FROM EventChestConfig ecc
	    WHERE ecc.eventItemMap.event.eventId = :eventId
	    AND ecc.deleted = false
	    ORDER BY ecc.eventItemMap.category,
	             ecc.gender
	""")
	List<EventChestConfigDTO> findAllDTOByEventId(Long eventId);
    
    
    @Modifying
    @Transactional
    @Query("""
        DELETE FROM EventChestConfig ecc
        WHERE ecc.eventItemMap.event.eventId = :eventId
          AND ecc.eventItemMap.eimId NOT IN :eimIds
    """)
    void deleteMissingConfigs(
            @Param("eventId") Long eventId,
            @Param("eimIds") List<Long> eimIds
    );
    
    
    

}