package com.dms.kalari.events.repository;

import com.dms.kalari.admin.entity.CoreUser;
import com.dms.kalari.common.BaseRepository;
import com.dms.kalari.events.entity.EventItemMap;
import com.dms.kalari.events.entity.MemberEventItem;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface MemberEventItemRepository extends BaseRepository<MemberEventItem, Long> {

	/**
	 * Fetch a single MemberEventItem by id, ensuring it is not soft-deleted.
	 */
	@Query("SELECT m FROM MemberEventItem m " + "WHERE m.meiId = :id AND m.deleted = false")
	Optional<MemberEventItem> findByIdAndNotDeleted(@Param("id") Long id);

	@Query("SELECT m FROM MemberEventItem m " + "WHERE m.memberEvent.eventId = :eventId "
			+ "AND m.memberEventNode.nodeId = :nodeId " + "AND m.deleted = false")
	List<MemberEventItem> findByEventAndNode(@Param("eventId") Long eventId, @Param("nodeId") Long nodeId);

	@Query("""
			    SELECT CONCAT(m.memberEventMap.eimId, '-', m.memberEventMember.userId)
			    FROM MemberEventItem m
			    WHERE m.memberEvent.id = :eventId
			      AND m.memberEventNode.id = :nodeId
			      AND m.memberEventItem.evitemId = :itemId
			      AND m.deleted = false
			""")
	Set<String> findKeysByEventAndNode(@Param("eventId") Long eventId, @Param("nodeId") Long nodeId,
			@Param("itemId") Long itemId);

	@Modifying
	@Transactional
	@Query("""
			    DELETE FROM MemberEventItem m
			    WHERE m.memberEventMap.eimId = :eventItemId
			      AND m.memberEventMember.userId = :userId
			      AND m.memberEventMap.event.id = :eventId
			      AND m.memberEventNode.id = :nodeId
			""")
//@todo  add condition grade and score is empty

	void deleteByEventItemAndUserAndEventAndNode(@Param("eventItemId") Long eventItemId, @Param("userId") Long userId,
			@Param("eventId") Long eventId, @Param("nodeId") Long nodeId);

	@Modifying
	@Transactional
	@Query("""
			    DELETE FROM MemberEventItem m
			     WHERE CONCAT(m.memberEventMap.id, '-', m.memberEventMember.id) IN :keys
			       AND (m.memberEventScore IS NULL OR m.memberEventScore = 0)
			       AND  m.memberEventGrade IS NULL
			       AND  m.memberEventItem.evitemId = :itemId
			""")
	int deleteByKeys(@Param("keys") Collection<String> keys, @Param("itemId") Long itemId);

	@Query("""
			    SELECT mei
			    FROM MemberEventItem mei
			    JOIN FETCH mei.memberEventItem ei
			    JOIN FETCH mei.memberEventMember cu
			    JOIN FETCH mei.memberEventNode n
			    WHERE mei.memberEvent.eventId = :eventId
			      AND (:itemId IS NULL OR ei.evitemId = :itemId)
			      AND (:gender IS NULL OR mei.memberEventGender = :gender)
			      AND (:category IS NULL OR mei.memberEventCategory = :category)
			    ORDER BY
			        ei.evitemName ASC,
			        mei.memberEventGender ASC,
			        mei.memberEventCategory ASC,
			        n.nodeName ASC,
			        cu.userFname ASC
			""")
	List<MemberEventItem> findByEventIdWithFilters(@Param("eventId") Long eventId, @Param("itemId") Long itemId,
			@Param("gender") CoreUser.Gender gender, @Param("category") EventItemMap.Category category);

}
