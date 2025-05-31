package com.example.tenant_service.service;

import com.example.tenant_service.common.BaseService;
import com.example.tenant_service.dto.MemberEventDTO;
import com.example.tenant_service.entity.CoreUser;
import com.example.tenant_service.entity.MemberEvent;
import com.example.tenant_service.mapper.MemberEventMapper;
import com.example.tenant_service.repository.MemberEventRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberEventService implements BaseService<MemberEventDTO> {

    private final MemberEventRepository memberEventRepository;
    private final MemberEventMapper memberEventMapper;

    @Override
    @Transactional
    public MemberEventDTO save(MemberEventDTO dto) {
        MemberEvent entity = memberEventMapper.toEntity(dto);
        entity.setApplyDate(LocalDateTime.now());
        MemberEvent saved = memberEventRepository.save(entity);
        return memberEventMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public MemberEventDTO update(Long id, MemberEventDTO dto) {
        MemberEvent existing = memberEventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MemberEvent not found with id: " + id));
        memberEventMapper.updateEntityFromDto(dto, existing);
        MemberEvent updated = memberEventRepository.save(existing);
        return memberEventMapper.toDTO(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberEventDTO> findAll() {
        return memberEventRepository.findAll().stream()
                .map(memberEventMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MemberEventDTO> findAllPaginate(Pageable pageable, String search) {
        return memberEventRepository.findAll(pageable)
                .map(memberEventMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberEventDTO findById(Long id) {
        return memberEventRepository.findById(id)
                .map(memberEventMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("MemberEvent not found with id: " + id));
    }

    @Override
    @Transactional
    public void softDeleteById(Long id) {
        MemberEvent entity = memberEventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MemberEvent not found with id: " + id));
        entity.setDeleted(true);
        memberEventRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public List<MemberEventDTO> findByMember(Long memberId) {
        return memberEventRepository.findByMemberByUserId(memberId).stream()
                .map(memberEventMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MemberEventDTO> findByNode(Long nodeId) {
        return memberEventRepository.findByNodeByNodeId(nodeId).stream()
                .map(memberEventMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MemberEventDTO> findByEvent(Long eventId) {
        return memberEventRepository.findByEventByEventId(eventId).stream()
                .map(memberEventMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public MemberEventDTO findByEventAndMember(Long eventId, Long memberId) {
        return memberEventRepository.findByEventByEventAndMemberId(eventId, memberId)
                .map(memberEventMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("MemberEvent not found with eventId: " + eventId + " and memberId: " + memberId));
    }


    @Transactional
    public MemberEventDTO approveApplication(Long id, Long approvedByUserId) {
        MemberEvent entity = memberEventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MemberEvent not found with id: " + id));
        CoreUser approver = new CoreUser();
        approver.setUserId(approvedByUserId);
        entity.setApprovedBy(approver);
        entity.setApprovedDate(LocalDateTime.now());
        return memberEventMapper.toDTO(memberEventRepository.save(entity));
    }

    @Transactional
    public MemberEventDTO enterResult(Long id, Map<Integer, String> items, Long enteredByUserId) {
        MemberEvent entity = memberEventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MemberEvent not found with id: " + id));
        CoreUser entrant = new CoreUser();
        entrant.setUserId(enteredByUserId);
        entity.setItems(
            items.entrySet().stream()
                 .collect(Collectors.toMap(e -> e.getKey().toString(), Map.Entry::getValue))
        );
        entity.setResultEntryBy(entrant);
        entity.setResultDate(LocalDateTime.now());
        return memberEventMapper.toDTO(memberEventRepository.save(entity));
    }

    @Transactional
    public MemberEventDTO approveResult(Long id, Long approvedByUserId) {
        MemberEvent entity = memberEventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MemberEvent not found with id: " + id));
        CoreUser approver = new CoreUser();
        approver.setUserId(approvedByUserId);
        entity.setResultApprovedBy(approver);
        entity.setResultApprovalDate(LocalDateTime.now());
        return memberEventMapper.toDTO(memberEventRepository.save(entity));
    }

    @Transactional(readOnly = true)
    public List<MemberEventDTO> getPendingApprovals() {
        return memberEventRepository.findByApprovedByIsNull().stream()
                .map(memberEventMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MemberEventDTO> getPendingResultApprovals() {
        return memberEventRepository
                .findByResultApprovedByIsNullAndResultEntryByIsNotNull()
                .stream()
                .map(memberEventMapper::toDTO)
                .collect(Collectors.toList());
    }
}
