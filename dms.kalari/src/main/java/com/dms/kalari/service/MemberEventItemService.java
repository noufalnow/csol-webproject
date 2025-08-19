package com.dms.kalari.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dms.kalari.entity.MemberEventItem;
import com.dms.kalari.exception.ResourceNotFoundException;
import com.dms.kalari.repository.MemberEventItemRepository;
import com.dms.kalari.repository.MemberEventRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MemberEventItemService {

    private final MemberEventItemRepository memberEventItemRepository;
    private final MemberEventRepository memberEventRepository;

    public MemberEventItemService(MemberEventItemRepository memberEventItemRepository,
                                MemberEventRepository memberEventRepository) {
        this.memberEventItemRepository = memberEventItemRepository;
        this.memberEventRepository = memberEventRepository;
    }

    @Transactional(readOnly = true)
    public Optional<MemberEventItem> findById(Long id) {
        return memberEventItemRepository.findByIdAndNotDeleted(id);
    }

    @Transactional(readOnly = true)
    public Page<MemberEventItem> findAll(Pageable pageable) {
        return memberEventItemRepository.findAllNotDeleted(pageable);
    }

    /*@Transactional(readOnly = true)
    public List<MemberEventItem> findByMemberEventId(Long eventId) {
        if (!memberEventRepository.existsByIdAndNotDeleted(eventId)) {
            throw new ResourceNotFoundException("MemberEvent not found with id: " + eventId, eventId);
        }
        return memberEventItemRepository.findByMemberEventIdAndNotDeleted(eventId);
    }*/

    @Transactional(readOnly = true)
    public List<MemberEventItem> findByMemberId(Long memberId) {
        return memberEventItemRepository.findByMemberIdAndNotDeleted(memberId);
    }

    @Transactional(readOnly = true)
    public List<MemberEventItem> findByEventIdAndMemberId(Long eventId, Long memberId) {
        return memberEventItemRepository.findByEventIdAndMemberIdAndNotDeleted(eventId, memberId);
    }

    @Transactional(readOnly = true)
    public List<MemberEventItem> findByEntryDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return memberEventItemRepository.findByEntryDateRangeAndNotDeleted(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<MemberEventItem> findByItemKey(Integer itemKey) {
        return memberEventItemRepository.findByItemKeyAndNotDeleted(itemKey);
    }

    @Transactional(readOnly = true)
    public Optional<MemberEventItem> findByUniqueId(String uniqueId) {
        return memberEventItemRepository.findByUniqueIdAndNotDeleted(uniqueId);
    }

    @Transactional(readOnly = true)
    public List<MemberEventItem> findUnapprovedItemsByEventId(Long eventId) {
        return memberEventItemRepository.findUnapprovedItemsByEventId(eventId);
    }

    /*@Transactional
    public MemberEventItem create(MemberEventItem memberEventItem) {
        // Validate the associated MemberEvent exists
        Map<String, String> eventId = memberEventItem.getMemberEvent().getItems();
        if (!memberEventRepository.existsByIdAndNotDeleted(eventId)) {
            throw new ResourceNotFoundException("MemberEvent not found with id: " + eventId,eventId);
        }

        // Set creation-related fields
        memberEventItem.setDeleted(false);
        memberEventItem.setEntryDateTime(LocalDateTime.now());
        
        return memberEventItemRepository.save(memberEventItem);
    }*/

    @Transactional
    public MemberEventItem update(Long id, MemberEventItem updatedItem) {
        return memberEventItemRepository.findByIdAndNotDeleted(id)
                .map(existingItem -> {
                    // Update only the fields that should be modified
                    existingItem.setItemKey(updatedItem.getItemKey());
                    existingItem.setItemValue(updatedItem.getItemValue());
                    existingItem.setScore(updatedItem.getScore());
                    
                    // Approval fields should be updated through separate approve method
                    return memberEventItemRepository.save(existingItem);
                })
                .orElseThrow(() -> new ResourceNotFoundException("MemberEventItem not found with id: " + id, id));
    }

    @Transactional
    public MemberEventItem approveItem(Long id, Long approvedByUserId) {
        return memberEventItemRepository.findByIdAndNotDeleted(id)
                .map(item -> {
                    item.setApproveDateTime(LocalDateTime.now());
                    // Assuming you have a method to set approvedBy with CoreUser
                    // item.setApprovedBy(getCoreUserById(approvedByUserId));
                    return memberEventItemRepository.save(item);
                })
                .orElseThrow(() -> new ResourceNotFoundException("MemberEventItem not found with id: " + id, id));
    }

    @Transactional
    public void delete(Long id) {
        MemberEventItem item = memberEventItemRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("MemberEventItem not found with id: " + id, id));
        
        item.setDeleted(true);
        memberEventItemRepository.save(item);
    }
}