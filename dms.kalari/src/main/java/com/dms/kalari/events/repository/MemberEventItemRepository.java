package com.dms.kalari.events.repository;

import com.dms.kalari.admin.entity.CoreUser;
import com.dms.kalari.admin.entity.CoreUser.Gender;
import com.dms.kalari.branch.entity.Node;
import com.dms.kalari.common.BaseRepository;
import com.dms.kalari.events.entity.Event;
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
import java.nio.file.Path;

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
		      AND (:memberNode IS NULL OR n.nodeId = :memberNode)

                    ORDER BY
                        CASE
                            WHEN :viewType IN ('tabsheet', 'finallist')
                            THEN mei.memberEventScore
                        END DESC NULLS LAST,
                    
                      mei.memberChestNo ASC,
		      n.nodeName ASC,
		      ei.evitemName ASC,
		      mei.memberEventGender ASC,
		      mei.memberEventCategory ASC,
		      mei.memberEventTeamCode ASC,
		      cu.userFname ASC
		    """)
		List<MemberEventItem> findByEventIdWithFilters(
		        @Param("eventId") Long eventId,
		        @Param("itemId") Long itemId,
		        @Param("gender") CoreUser.Gender gender,
		        @Param("category") EventItemMap.Category category,
		        @Param("memberNode") Long memberNode,
		        @Param("viewType") String viewType
		);

	@Query("""
		    SELECT mei FROM MemberEventItem mei
		    JOIN FETCH mei.memberEvent me
		    JOIN FETCH mei.memberEventHost meh
		    JOIN FETCH mei.memberEventMember mem
		    WHERE me.eventId = :eventId
		      AND mei.memberEventGrade IS NOT NULL
		      AND mei.certificateStatus IS NULL
		      AND mei.verificationStatus = :verification
		      AND mei.deleted = false
		""")
		List<MemberEventItem> findByEventIdWhereGradeNotEmpty(
		    @Param("eventId") Long eventId,
		    @Param("verification") MemberEventItem.VerificationStatus verification
		);


	@Query("""
			SELECT mei FROM MemberEventItem mei
			JOIN FETCH mei.memberEvent me
			JOIN FETCH mei.memberEventHost meh
			JOIN FETCH mei.memberEventMember mem
			WHERE mei.meiId = :meiId
			  AND mei.deleted = false
			""")
	Optional<MemberEventItem> findByIdWithEagerAssociations(@Param("meiId") Long meiId);

	@Modifying
	@Transactional
	@Query("""
	    UPDATE MemberEventItem m
	    SET m.certificateStatus = :status,
	        m.certificateHistoryJson = :historyJson,
	        m.meiCertificatePath = :filePath,
	        m.meiCertificateFile = :fileName
	    WHERE m.meiId = :meiId
	""")
	void updateCertificateStatusAndHistory(
	        @Param("meiId") Long meiId,
	        @Param("status") MemberEventItem.CertificateStatus status,
	        @Param("historyJson") String historyJson,
	        @Param("filePath") String filePath,
	        @Param("fileName") String fileName
	);
	
	@Query("""
		    SELECT m.meiId 
		    FROM MemberEventItem m 
		    WHERE m.deleted = false
		      AND m.memberEvent.id = :eventId
		      AND m.verificationStatus = com.dms.kalari.events.entity.MemberEventItem.VerificationStatus.APPROVED
		      AND (:itemId IS NULL OR m.memberEventItem.evitemId = :itemId)
		      AND (:gender IS NULL OR m.memberEventGender = :gender)
		      AND (:category IS NULL OR m.memberEventCategory = :category)
		""")
		List<Long> findApprovedVerificationIdsByFilters(
		        @Param("eventId") Long eventId,
		        @Param("itemId") Long itemId,
		        @Param("gender") CoreUser.Gender gender,
		        @Param("category") EventItemMap.Category category);
	
	
	@Query("""
		    SELECT CONCAT(m.memberEventItem, '-', m.memberEventCategory, '-', m.memberEventMember)
		    FROM MemberEventItem m
		    WHERE (:eventYear IS NULL OR m.memberEventYear = :eventYear)
		      AND (:nodeId IS NULL OR m.memberEventNode.nodeId = :nodeId)
		      AND (:hostType IS NULL OR m.memberHostType = :hostType)
		      AND m.verificationStatus = com.dms.kalari.events.entity.MemberEventItem.VerificationStatus.APPROVED
		      AND m.memberEventGrade IN (
		          com.dms.kalari.events.entity.MemberEventItem.Grade.GOLD, 
		          com.dms.kalari.events.entity.MemberEventItem.Grade.SILVER, 
		          com.dms.kalari.events.entity.MemberEventItem.Grade.BRONZE)
		      AND m.deleted = false
		""")
		List<String> findMeiIdsByFilters(
		        @Param("eventYear") Integer eventYear,
		        @Param("nodeId") Long nodeId,
		        @Param("hostType") Event.Type hostType
		);
	
	
	
	
	@Query("SELECT m FROM MemberEventItem m " + "WHERE m.memberEvent.eventId = :eventId "
		 + "AND m.deleted = false")
	List<MemberEventItem> findByEventId(@Param("eventId") Long eventId);

	
	@Query("""
		    SELECT mei
		    FROM MemberEventItem mei
		    WHERE mei.deleted = false
		      AND mei.memberEventMap.eimId = :eimId
		      AND mei.memberEventGender = :gender
		    ORDER BY mei.meiId ASC
		""")
		List<MemberEventItem> findAllByEventItemAndGender(
		        @Param("eimId") Long eimId,
		        @Param("gender") Gender gender
		);
	
	@Query("""
		    SELECT mei
		    FROM MemberEventItem mei
		    WHERE mei.deleted = false
		      AND mei.memberEventMap.eimId = :eimId
		      AND mei.memberEventGender = :gender
		    ORDER BY 
			  mei.memberEventMember.userFname ASC
		""")
		List<MemberEventItem> findAllByEventItemAndGenderOrderTeamAndName(
		        @Param("eimId") Long eimId,
		        @Param("gender") Gender gender
		);
	
	
	@Query("""
		    SELECT m
		    FROM MemberEventItem m
		    WHERE m.memberEventMap.eimId = :eimId
		    AND m.memberEventMember.userId = :userId
		    AND m.deleted = false
		""")
		Optional<MemberEventItem> findByEventMapAndUser(
		        @Param("eimId") Long eimId,
		        @Param("userId") Long userId
		);
	
	
	@Query("""
		    SELECT m
		    FROM MemberEventItem m
		    WHERE m.memberEvent.eventId = :eventId
		    AND m.memberEventItem.evitemId <> :itemId
		    AND m.memberEventNode.nodeId = :nodeId
		    AND m.deleted = false
		""")
		List<MemberEventItem> findByEventExcludingItem(
		        @Param("eventId") Long eventId,
		        @Param("itemId") Long itemId,
		        @Param("nodeId") Long nodeId
		);
	
	
	@Modifying
	@Transactional
	@Query("""
	    DELETE
	    FROM MemberEventItem mei
	    WHERE mei.memberEvent.eventId = :eventId
	      AND mei.memberEventMember.userId = :memberId
	""")
	void deleteByEventAndMember(
	        @Param("eventId") Long eventId,
	        @Param("memberId") Long memberId
	);
	
        /*'IKF-',*/
	
	@Query(value = """
		UPDATE member_events_items mei
		SET mei_certificate_no =
		(
		    SELECT CONCAT(

		        mei.mei_event_year,
		        '-E',
		        LPAD(
		            CAST(mei.mei_event_id AS TEXT),
		            5,
		            '0'
		        ),
		        '-C',
		        LPAD(
		            CAST(
		                COALESCE(
		                    (
		                        SELECT COUNT(*) + 1
		                        FROM member_events_items x
		                        WHERE x.mei_event_id =
		                              mei.mei_event_id
		                        AND x.mei_certificate_no
		                            IS NOT NULL
		                    ),
		                    1
		                )
		                AS TEXT
		            ),
		            6,
		            '0'
		        )
		    )
		)
		WHERE mei_id=:meiId
		AND mei_certificate_no IS NULL
		RETURNING mei_certificate_no
		""", nativeQuery = true)
		String generateCertificateNumber(
		        @Param("meiId")
		        Long meiId
		);



// cd /usr/local/kafka/bin

//./kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic certificate-generate-topic

}
