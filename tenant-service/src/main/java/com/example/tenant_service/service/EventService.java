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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService implements BaseService<EventDTO> {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final NodeRepository nodeRepository;

    @Autowired
    public EventService(EventRepository eventRepository, 
                       EventMapper eventMapper,
                       NodeRepository nodeRepository) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.nodeRepository = nodeRepository;
    }

    @Override
    public EventDTO update(Long eventId, EventDTO eventDTO) {
        Event existingEvent = eventRepository.findByIdAndNotDeleted(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", eventId));

        // Update host node if changed
        if (eventDTO.getEventHostId() != null && 
            !eventDTO.getEventHostId().equals(existingEvent.getHostNode().getNodeId())) {
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
        return eventRepository.findAllNotDeleted(null, Pageable.unpaged()).stream()
                .map(eventMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Page<EventDTO> findAllPaginate(Pageable pageable, String search) {
        return eventRepository.findAllNotDeleted(search, pageable)
                .map(eventMapper::toDTO);
    }

    @Override
    public EventDTO findById(Long eventId) {
        return eventRepository.findByIdAndNotDeleted(eventId)
                .map(eventMapper::toDTO)
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
        return eventRepository.findByHostNodeIdAndNotDeleted(nodeId).stream()
                .map(eventMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<EventDTO> findByYear(Integer year) {
        return eventRepository.findByYearAndNotDeleted(year).stream()
                .map(eventMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<EventDTO> findActiveEventsOnDate(LocalDate date) {
        return eventRepository.findActiveEventsOnDate(date).stream()
                .map(eventMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<EventDTO> findByHostTypeAndHostId(String hostType, Long hostId) {
        return eventRepository.findByHostTypeAndHostId(hostType, hostId).stream()
                .map(eventMapper::toDTO)
                .collect(Collectors.toList());
    }

    /*public boolean isEventNameAvailable(String eventName, Long excludeEventId) {
        if (excludeEventId != null) {
            return !eventRepository.existsByEventNameAndIdNotAndDeletedFalse(eventName, excludeEventId);
        }
        return !eventRepository.existsByEventNameAndDeletedFalse(eventName);
    }*/
}