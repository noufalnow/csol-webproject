package com.dms.kalari.events.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dms.kalari.admin.entity.CoreUser;
import com.dms.kalari.branch.entity.Node;
import com.dms.kalari.branch.repository.NodeRepository;
import com.dms.kalari.common.BaseService;
import com.dms.kalari.events.dto.EventDTO;
import com.dms.kalari.events.dto.EventItemDTO;
import com.dms.kalari.events.entity.Event;
import com.dms.kalari.events.entity.EventItem;
import com.dms.kalari.events.entity.EventItemMap;
import com.dms.kalari.events.mapper.EventItemMapper;
import com.dms.kalari.events.mapper.EventMapper;
import com.dms.kalari.events.repository.EventItemMapRepository;
import com.dms.kalari.events.repository.EventRepository;
import com.dms.kalari.exception.ResourceNotFoundException;

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
	private final EventItemMapRepository eventItemMapRepository;
	
	

	@Autowired
	public EventService(EventRepository eventRepository, EventMapper eventMapper, NodeRepository nodeRepository,
			EntityManager entityManager, EventItemMapRepository eventItemMapRepository) {
		this.eventRepository = eventRepository;
		this.eventMapper = eventMapper;
		this.nodeRepository = nodeRepository;
		this.entityManager = entityManager;
		this.eventItemMapRepository = eventItemMapRepository;
	}
	
	@Autowired
	private EventItemMapper eventItemMapper;

	@Override
	public EventDTO update(Long eventId, EventDTO eventDTO) {
	    // Find existing event or throw exception
	    Event existingEvent = eventRepository.findByIdAndNotDeleted(eventId)
	            .orElseThrow(() -> new ResourceNotFoundException("Event", eventId));

	    // Update individual fields from DTO
	    existingEvent.setEventName(eventDTO.getEventName());
	    existingEvent.setEventPeriodStart(eventDTO.getEventPeriodStart());
	    existingEvent.setEventPeriodEnd(eventDTO.getEventPeriodEnd());
	    existingEvent.setEventVenue(eventDTO.getEventVenue());
	    existingEvent.setEventOfficialPhone(eventDTO.getEventOfficialPhone());
	    existingEvent.setEventYear(eventDTO.getEventYear());
	    
	    // Set modification timestamp
	    existingEvent.setTModified(LocalDateTime.now());

	    // Save and return updated event
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
	
	public EventDTO findByIdWithItemsByCategory(Long eventId, EventItemMap.Category category) {
		
	    Event event = eventRepository.findByIdWithItemsByCategory(eventId, category)
	        .orElseThrow(() -> new ResourceNotFoundException("Event", eventId));

	    EventDTO dto = eventMapper.toDTO(event);

	    List<EventItemDTO> categoryItems = event.getEventItemMaps().stream()
	        .map(m -> eventItemMapper.toDTO(m.getItem()))
	        .toList();

	    // set the list depending on the category
	    if (category == EventItemMap.Category.JUNIOR) {
	        dto.setJuniorEventItems(categoryItems);
	    } else if (category == EventItemMap.Category.SENIOR) {
	        dto.setSeniorEventItems(categoryItems);
	    } else if (category == EventItemMap.Category.SUBJUNIOR) {
	        dto.setSubjuniorEventItems(categoryItems);
	    }
	    

	    return dto;
	}
	
	public List<EventItemDTO> getEventItemsByCategory(Long eventId, EventItemMap.Category category) {
	    List<EventItem> items = eventItemMapRepository.findItemsByEventIdAndCategory(eventId, category);
	    return items.stream()
	                .map(eventItemMapper::toDTO)
	                .collect(Collectors.toList());
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
							  e.event_id AS event_id,
							  me.memvnt_id AS memvnt_id,
							  me.memvnt_item AS memvnt_item,
							  me.memvnt_event_id AS memvnt_event_id,
							  me.memvnt_member_id AS memvnt_member_id,
							  me.memvnt_member_node_id AS memvnt_member_node_id,
							  me.memvnt_node_id AS memvnt_node_id,
							  kv.key::int AS item_id,
							  ev.evitem_name AS item_name,
							  kv.value AS item_result,
							  CASE kv.value
							    WHEN 'A' THEN 'Gold'
							    WHEN 'B' THEN 'Silver'
							    WHEN 'C' THEN 'Bronze'
							    WHEN 'P' THEN 'Participation'
							    ELSE 'Pending'
							  END AS item_status_label,
							  user_fname || ' ' || user_lname AS member_name,
							  mn.node_name AS member_node_name,
							  en.node_name AS event_node_name,
							  e.event_name AS event_name,
							  TO_CHAR(me.memvnt_result_date, 'DD/MM/YYYY') AS memvnt_result_date
							FROM member_events me
							LEFT JOIN core_users AS member ON member.user_id = me.memvnt_member_id
							LEFT JOIN events e ON e.event_id = me.memvnt_event_id
							LEFT JOIN nodes mn ON mn.node_id = me.memvnt_member_node_id
							LEFT JOIN nodes en ON en.node_id = me.memvnt_node_id
							JOIN LATERAL jsonb_each_text(me.memvnt_item::jsonb) AS kv(key, value) ON TRUE
							LEFT JOIN event_items ev ON ev.evitem_id = kv.key::int
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
	
	
	public List<Object[]> findAllEventsApplicable(Long nodeId) {
		// return eventRepository.findEventsByMemberAndNodeHierarchy(nodeId, userId);

		StringBuilder queryBuilder = new StringBuilder(
				"""
			WITH RECURSIVE
			-- Upward traversal: node → all ancestors
			up_path AS (
			    SELECT node_id, parent_id
			    FROM nodes
			    WHERE node_id = :nodeId
			    UNION ALL
			    SELECT n.node_id, n.parent_id
			    FROM nodes n
			    JOIN up_path u ON n.node_id = u.parent_id     -- follow only the parent link
			    WHERE n.deleted = false
			),
			
			-- Downward traversal: node → all descendants
			down_path AS (
			    SELECT node_id, parent_id
			    FROM nodes
			    WHERE node_id = :nodeId
			    UNION ALL
			    SELECT n.node_id, n.parent_id
			    FROM nodes n
			    JOIN down_path d ON n.parent_id = d.node_id
			    WHERE n.deleted = false
			),
			
			-- Combine both directions, removing duplicates
			full_hierarchy AS (
			    SELECT node_id FROM up_path
			    UNION
			    SELECT node_id FROM down_path
			)
			
			SELECT
			    e.*,
			    TO_CHAR(e.event_period_start, 'DD/MM/YYYY') AS dd_event_period_start,
			    TO_CHAR(e.event_period_end,   'DD/MM/YYYY') AS dd_event_period_end,
			    nd.node_name
			FROM events e
			JOIN full_hierarchy fh
			    ON e.event_host_id = fh.node_id
			    
			JOIN nodes nd
			    ON fh.node_id = nd.node_id
			    
			WHERE e.deleted = false;

						 """);

		Query query = entityManager.createNativeQuery(queryBuilder.toString(), Tuple.class);

		query.setParameter("nodeId", nodeId);

		return query.getResultList();

	}
	
}