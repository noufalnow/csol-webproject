package com.example.tenant_service.service;

import com.example.tenant_service.exception.ResourceNotFoundException;
import com.example.tenant_service.common.BaseService;
import com.example.tenant_service.dto.EventDTO;
import com.example.tenant_service.entity.Event;
import com.example.tenant_service.entity.Node;
import com.example.tenant_service.mapper.EventMapper;
import com.example.tenant_service.repository.EventRepository;
import com.example.tenant_service.repository.NodeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;

@Service
public class EventService implements BaseService<EventDTO> {

	private final EventRepository eventRepository;
	private final EventMapper eventMapper;
	private final NodeRepository nodeRepository;
	private final EntityManager entityManager;

	@Autowired
	public EventService(EventRepository eventRepository, EventMapper eventMapper, NodeRepository nodeRepository,
			EntityManager entityManager) {
		this.eventRepository = eventRepository;
		this.eventMapper = eventMapper;
		this.nodeRepository = nodeRepository;
		this.entityManager = entityManager;
	}

	@Override
	public EventDTO update(Long eventId, EventDTO eventDTO) {
		Event existingEvent = eventRepository.findByIdAndNotDeleted(eventId)
				.orElseThrow(() -> new ResourceNotFoundException("Event", eventId));

		// Update host node if changed
		if (eventDTO.getEventHostId() != null
				&& !eventDTO.getEventHostId().equals(existingEvent.getHostNode().getNodeId())) {
			Node hostNode = nodeRepository.findById(eventDTO.getEventHostId())
					.orElseThrow(() -> new ResourceNotFoundException("Node", eventDTO.getEventHostId()));
			existingEvent.setHostNode(hostNode);
		}

		eventMapper.updateEventFromDto(eventDTO, existingEvent);
		existingEvent.setTModified(LocalDateTime.now());

		Event updatedEvent = eventRepository.save(existingEvent);
		return eventMapper.toDTO(updatedEvent);
	}

	@Override
	public List<EventDTO> findAll() {
		return eventRepository.findAllNotDeleted(null, Pageable.unpaged()).stream().map(eventMapper::toDTO)
				.collect(Collectors.toList());
	}

	public Page<EventDTO> findAllPaginate(Pageable pageable, String search) {
		return eventRepository.findAllNotDeleted(search, pageable).map(eventMapper::toDTO);
	}

	@Override
	public EventDTO findById(Long eventId) {
		return eventRepository.findByIdAndNotDeleted(eventId).map(eventMapper::toDTO)
				.orElseThrow(() -> new ResourceNotFoundException("Event", eventId));
	}

	@Override
	public EventDTO save(EventDTO eventDTO) {
		Event event = eventMapper.toEntity(eventDTO);
		Event savedEvent = eventRepository.save(event);
		return eventMapper.toDTO(savedEvent);
	}

	@Override
	public void softDeleteById(Long eventId) {
		Event event = eventRepository.findByIdAndNotDeleted(eventId)
				.orElseThrow(() -> new ResourceNotFoundException("Event", eventId));

		event.setDeleted(true);
		event.setTDeleted(LocalDateTime.now());
		eventRepository.save(event);
	}

	// Additional business logic methods specific to events
	public List<EventDTO> findByHostNode(Long nodeId) {
		return eventRepository.findByHostNodeIdAndNotDeleted(nodeId).stream().map(eventMapper::toDTO)
				.collect(Collectors.toList());
	}

	public List<Object[]> findByHostNodeHierarchy(Long nodeId, Long userId) {
		// return eventRepository.findEventsByMemberAndNodeHierarchy(nodeId, userId);

		StringBuilder queryBuilder = new StringBuilder(
				"""
						WITH RECURSIVE node_hierarchy AS (
						    SELECT node_id, parent_id
						    FROM nodes
						    WHERE node_id = :nodeId
						    UNION ALL
						    SELECT n.node_id, n.parent_id
						    FROM nodes n
						    INNER JOIN node_hierarchy nh ON n.node_id = nh.parent_id
						    WHERE n.deleted = false
						)
						SELECT e.*, me.*, TO_CHAR("event_period_start", 'DD/MM/YYYY') AS dd_event_period_start, TO_CHAR("event_period_end", 'DD/MM/YYYY') AS dd_event_period_end
						FROM events e
						LEFT JOIN member_events me ON me.memvnt_event_id = e.event_id
						    AND me.memvnt_member_id = :memberId
						    AND me.deleted = false
						JOIN node_hierarchy nh ON e.event_host_id = nh.node_id
						WHERE e.deleted = false
						 """);

		// queryBuilder.append(" ORDER BY ").append(sortField).append("
		// ").append(sortDir);

		Query query = entityManager.createNativeQuery(queryBuilder.toString(), Tuple.class);

		query.setParameter("nodeId", nodeId);
		query.setParameter("memberId", userId);

		return query.getResultList();

	}

	public List<EventDTO> findByYear(Integer year) {
		return eventRepository.findByYearAndNotDeleted(year).stream().map(eventMapper::toDTO)
				.collect(Collectors.toList());
	}

	public List<EventDTO> findActiveEventsOnDate(LocalDate date) {
		return eventRepository.findActiveEventsOnDate(date).stream().map(eventMapper::toDTO)
				.collect(Collectors.toList());
	}

	public List<EventDTO> findByHostTypeAndHostId(String hostType, Long hostId) {
		return eventRepository.findByHostTypeAndHostId(hostType, hostId).stream().map(eventMapper::toDTO)
				.collect(Collectors.toList());
	}

	/*
	 * public boolean isEventNameAvailable(String eventName, Long excludeEventId) {
	 * if (excludeEventId != null) { return
	 * !eventRepository.existsByEventNameAndIdNotAndDeletedFalse(eventName,
	 * excludeEventId); } return
	 * !eventRepository.existsByEventNameAndDeletedFalse(eventName); }
	 */

	public List<Object[]> getMemberEventsWithFilters(Long itemId, Long eventId, Long memberId, Long memberNodeId,
			Long nodeId, Boolean approvedDatePresent, Long memvntId) {
		StringBuilder queryBuilder = new StringBuilder("""
				    SELECT
				      e.event_id as event_id,
				      me.memvnt_id as memvnt_id,
				      me.memvnt_item as memvnt_item,
				      me.memvnt_event_id as memvnt_event_id,
				      me.memvnt_member_id as memvnt_member_id,
				      me.memvnt_member_node_id as memvnt_member_node_id,
				      me.memvnt_node_id as memvnt_node_id,
				      kv.key::int as item_id,
				      user_fname || ' ' || user_lname as member_name,
				      CASE kv.key::int
				        WHEN 1 THEN 'CHUVADU'
				        WHEN 2 THEN 'HIGH KICK(CHAVITTIPONGHAL)'
				        WHEN 3 THEN 'MEYPPAYATTU'
				        WHEN 4 THEN 'WIELDINGOF URUMI (FLEXIBLE SWORD)'
				        WHEN 5 THEN 'SWORD & SWORD (Seniors only)'
				        WHEN 6 THEN 'SWORD & SHIELD'
				        WHEN 7 THEN 'URUMI & SHIELD'
				        WHEN 8 THEN 'LONG STAFF FIGHT'
				        WHEN 9 THEN 'KURUVADI (SHORT STAFF FIGHT)'
				        WHEN 10 THEN 'KAIPPORU/UNARMED COMBAT (Seniors only)'
				        ELSE 'UNKNOWN'
				      END as item_name,
				      kv.value as item_result,
				      CASE kv.value
				        WHEN 'A' THEN 'Gold'
				        WHEN 'B' THEN 'Silver'
				        WHEN 'C' THEN 'Bronze'
				        WHEN 'P' THEN 'Participation'
				        ELSE 'Pending'
				      END as item_status_label,
				      mn.node_name as member_node_name,
				      en.node_name as event_node_name,
				      e.event_name as event_name,
				      TO_CHAR("memvnt_result_date", 'DD/MM/YYYY')  as memvnt_result_date
				    FROM member_events me
				    LEFT JOIN core_users as member on member.user_id = me.memvnt_member_id
				    LEFT JOIN events e ON e.event_id = me.memvnt_event_id
				    LEFT JOIN nodes mn ON mn.node_id = me.memvnt_member_node_id
				    LEFT JOIN nodes en ON en.node_id = me.memvnt_node_id,
				    jsonb_each_text(me.memvnt_item::jsonb) as kv
				    WHERE me.deleted = false
				""");

		// Add dynamic conditions
		if (itemId != null) {  // New condition for item
		    queryBuilder.append(" AND kv.key::int = :itemId");
		}
		if (memvntId != null) {  // New condition for item
		    queryBuilder.append(" AND me.memvnt_id = :memvntId");
		}
		if (eventId != null) {
			queryBuilder.append(" AND me.memvnt_event_id = :eventId");
		}
		if (memberId != null) {
			queryBuilder.append(" AND me.memvnt_member_id = :memberId");
		}
		if (memberNodeId != null) {
			queryBuilder.append(" AND me.memvnt_member_node_id = :memberNodeId");
		}
		if (nodeId != null) {
			queryBuilder.append(" AND me.memvnt_node_id = :nodeId");
		}
		if (approvedDatePresent != null) {
			queryBuilder.append(approvedDatePresent ? " AND me.memvnt_result_date IS NOT NULL"
					: " AND me.memvnt_result_date IS NULL");
		}

		queryBuilder.append(" ORDER BY item_name ASC");

		Query query = entityManager.createNativeQuery(queryBuilder.toString(), Tuple.class);

		// Set parameters
		if (itemId != null) 
			query.setParameter("itemId", itemId);
		if (memvntId != null) 
			query.setParameter("memvntId", memvntId);
		if (eventId != null)
			query.setParameter("eventId", eventId);
		if (memberId != null)
			query.setParameter("memberId", memberId);
		if (memberNodeId != null)
			query.setParameter("memberNodeId", memberNodeId);
		if (nodeId != null)
			query.setParameter("nodeId", nodeId);

		return query.getResultList();
	}
}