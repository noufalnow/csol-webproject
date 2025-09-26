package com.dms.kalari.events.service;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dms.kalari.common.BaseService;
import com.dms.kalari.events.dto.EventItemMapDTO;
import com.dms.kalari.events.entity.Event;
import com.dms.kalari.events.entity.EventItem;
import com.dms.kalari.events.entity.EventItemMap;
import com.dms.kalari.events.entity.EventItemMap.Category;
import com.dms.kalari.events.mapper.EventItemMapMapper;
import com.dms.kalari.events.repository.EventItemMapRepository;
import com.dms.kalari.events.repository.EventItemRepository;
import com.dms.kalari.events.repository.EventRepository;
import com.dms.kalari.exception.ResourceNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class EventItemMapService implements BaseService<EventItemMapDTO> {

	private final EventItemMapRepository mapRepository;
	private final EventRepository eventRepository;
	private final EventItemRepository itemRepository;
	private final EventItemMapMapper mapMapper;

	@Autowired
	public EventItemMapService(EventItemMapRepository mapRepository, EventRepository eventRepository,
			EventItemRepository itemRepository, EventItemMapMapper mapMapper) {

		this.mapRepository = mapRepository;
		this.eventRepository = eventRepository;
		this.itemRepository = itemRepository;
		this.mapMapper = mapMapper;
	}

	public void saveMappings(Long eventId, List<Long> itemIds, EventItemMap.Category category) {
	    if (itemIds == null || itemIds.isEmpty()) return;

	    Event event = eventRepository.findById(eventId)
	            .orElseThrow(() -> new ResourceNotFoundException("Event", eventId));

	    List<Long> existingItemIds = mapRepository.findItemIdsByEventAndCategory(eventId, category);
	    List<Long> newIds = itemIds.stream()
	            .filter(id -> !existingItemIds.contains(id))
	            .toList();

	    if (newIds.isEmpty()) return;

	    List<EventItem> items = itemRepository.findAllById(newIds);
	    if (items.size() != newIds.size()) {
	        Set<Long> foundIds = items.stream()
	                                  .map(EventItem::getEvitemId)
	                                  .collect(Collectors.toSet());
	        List<Long> missingIds = newIds.stream()
	                                      .filter(id -> !foundIds.contains(id))
	                                      .toList();
	        throw new ResourceNotFoundException("EventItem", missingIds);
	    }

	    LocalDateTime now = LocalDateTime.now();
	    List<EventItemMap> mappings = items.stream().map(item -> {
	        EventItemMap mapping = new EventItemMap();
	        mapping.setEvent(event);
	        mapping.setItem(item);
	        mapping.setCategory(category);
	        mapping.setTCreated(now);
	        return mapping;
	    }).toList();

	    mapRepository.saveAll(mappings);
	}


	@Override
	public EventItemMapDTO update(Long id, EventItemMapDTO dto) {
		EventItemMap existing = mapRepository.findByIdAndNotDeleted(id)
				.orElseThrow(() -> new ResourceNotFoundException("EventItemMap", id));

		// If event has changed, re-fetch and set
		if (!existing.getEvent().getEventId().equals(dto.getEventId())) {
			Event event = eventRepository.findById(dto.getEventId())
					.orElseThrow(() -> new ResourceNotFoundException("Event", dto.getEventId()));
			existing.setEvent(event);
		}

		// If item has changed, re-fetch and set
		if (!existing.getItem().getEvitemId().equals(dto.getEvitemId())) {
			EventItem item = itemRepository.findById(dto.getEvitemId())
					.orElseThrow(() -> new ResourceNotFoundException("EventItem", dto.getEvitemId()));
			existing.setItem(item);
		}

		// Always update category
		existing.setCategory(dto.getCategory());
		existing.setTModified(LocalDateTime.now());

		EventItemMap updated = mapRepository.save(existing);
		return mapMapper.toDTO(updated);
	}

	@Override
	public List<EventItemMapDTO> findAll() {
		return mapRepository.findAllNotDeleted(Pageable.unpaged()).stream().map(mapMapper::toDTO)
				.collect(Collectors.toList());
	}

	@Override
	public EventItemMapDTO findById(Long id) {
		return mapRepository.findByIdAndNotDeleted(id).map(mapMapper::toDTO)
				.orElseThrow(() -> new ResourceNotFoundException("EventItemMap", id));
	}

	@Override
	public void softDeleteById(Long id) {
		EventItemMap entity = mapRepository.findByIdAndNotDeleted(id)
				.orElseThrow(() -> new ResourceNotFoundException("EventItemMap", id));
		entity.setDeleted(true);
		entity.setTDeleted(LocalDateTime.now());
		mapRepository.save(entity);
	}

	@Override
	public Page<EventItemMapDTO> findAllPaginate(Pageable pageable, String search) {
		// “search” is not used here; you can add a query filter if needed
		return mapRepository.findAllNotDeleted(pageable).map(mapMapper::toDTO);
	}

	@Override
	public EventItemMapDTO save(EventItemMapDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<EventItemMap.Category, List<Long>> findItemsByEventIdGroupedByCategory(Long eventId) {
		List<EventItemMap> mappings = mapRepository.findByEvent_EventId(eventId);

		return mappings.stream().collect(Collectors.groupingBy(EventItemMap::getCategory,
				Collectors.mapping(m -> m.getItem().getEvitemId(), Collectors.toList())));
	}

	@Modifying
	@Transactional
	public void deleteByEventIdAndCategory(
	        Long eventId,
	        EventItemMap.Category category,
	        List<Long> keepItemIds) {

	    if (keepItemIds == null || keepItemIds.isEmpty()) {
	        // nothing to keep: delete all rows for this category,
	        // still protecting those locked by MemberEventItem
	        mapRepository.deleteAllByEventAndCategoryExcludingMembers(eventId, category);
	    } else {
	        mapRepository.deleteUnselectedByCategory(eventId, category, keepItemIds);
	    }
	}



	public Map<EventItemMap.Category, List<EventItemMap>> getEventItemMatrix(Long eventId) {
	    // fetch all items for the event
	    List<EventItemMap> result = mapRepository.findByEventIdWithDetails(eventId);

	    // group them by category
	    return result.stream()
	                 .collect(Collectors.groupingBy(EventItemMap::getCategory));
	}



}
