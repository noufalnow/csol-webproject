package com.example.tenant_service.service;

import com.example.tenant_service.common.BaseService;
import com.example.tenant_service.dto.EventItemDTO;
import com.example.tenant_service.entity.EventItem;
import com.example.tenant_service.exception.ResourceNotFoundException;
import com.example.tenant_service.mapper.EventItemMapper;
import com.example.tenant_service.repository.EventItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventItemService implements BaseService<EventItemDTO> {

    private final EventItemRepository eventItemRepository;
    private final EventItemMapper eventItemMapper;

    @Autowired
    public EventItemService(EventItemRepository eventItemRepository, EventItemMapper eventItemMapper) {
        this.eventItemRepository = eventItemRepository;
        this.eventItemMapper = eventItemMapper;
    }

    @Override
    public EventItemDTO save(EventItemDTO eventItemDTO) {
        EventItem entity = eventItemMapper.toEntity(eventItemDTO);
        EventItem saved = eventItemRepository.save(entity);
        return eventItemMapper.toDTO(saved);
    }

    @Override
    public EventItemDTO update(Long id, EventItemDTO dto) {
        Optional<EventItem> optionalItem = eventItemRepository.findByIdAndNotDeleted(id);
        if (optionalItem.isEmpty()) {
            throw new ResourceNotFoundException("EventItem", id);
        }

        EventItem existing = optionalItem.get();

        existing.setTModified(LocalDateTime.now());
        existing.setEvitemName(dto.getEvitemName());
        existing.setEvitemCode(dto.getEvitemCode());
        existing.setEvitemDescription(dto.getEvitemDescription());
        existing.setEvitemCriteria(dto.getEvitemCriteria());
        existing.setTModified(LocalDateTime.now());

        EventItem updated = eventItemRepository.save(existing);
        return eventItemMapper.toDTO(updated);
    }

    @Override
    public List<EventItemDTO> findAll() {
        return eventItemRepository.findAllNotDeleted().stream()
                .map(eventItemMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EventItemDTO findById(Long id) {
        return eventItemRepository.findByIdAndNotDeleted(id)
                .map(eventItemMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("EventItem", id));
    }

    @Override
    public void softDeleteById(Long id) {
        EventItem eventItem = eventItemRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("EventItem", id));
        eventItem.setDeleted(true);
        eventItem.setTDeleted(LocalDateTime.now());
        eventItemRepository.save(eventItem);
    }


    @Override
    public Page<EventItemDTO> findAllPaginate(Pageable pageable, String search) {
        return eventItemRepository.findAllNotDeleted(search == null ? "" : search, pageable)
                .map(eventItemMapper::toDTO);
    }
}
