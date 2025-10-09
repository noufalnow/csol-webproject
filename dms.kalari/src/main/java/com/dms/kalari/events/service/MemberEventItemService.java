package com.dms.kalari.events.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dms.kalari.admin.entity.CoreUser;
import com.dms.kalari.admin.repository.CoreUserRepository;
import com.dms.kalari.branch.entity.Node;
import com.dms.kalari.branch.repository.NodeRepository;
import com.dms.kalari.events.entity.Event;
import com.dms.kalari.events.entity.EventItem;
import com.dms.kalari.events.entity.EventItemMap;
import com.dms.kalari.events.entity.MemberEvent;
import com.dms.kalari.events.entity.MemberEventItem;
import com.dms.kalari.events.repository.EventItemMapRepository;
import com.dms.kalari.events.repository.EventItemRepository;
import com.dms.kalari.events.repository.EventRepository;
import com.dms.kalari.events.repository.MemberEventItemRepository;
import com.dms.kalari.events.repository.MemberEventRepository;
import com.dms.kalari.exception.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MemberEventItemService {

	private final MemberEventItemRepository memberEventItemRepository;
	private final MemberEventRepository memberEventRepository;
	private final EventItemRepository eventItemRepository;
	private final CoreUserRepository coreUserRepository;
	private final NodeRepository nodeRepository;
	private final EventItemMapRepository eventItemMapRepository;
	private final EventRepository eventRepository;

	public MemberEventItemService(MemberEventItemRepository memberEventItemRepository,
			MemberEventRepository memberEventRepository, EventItemRepository eventItemRepository,
			CoreUserRepository coreUserRepository, NodeRepository nodeRepository,
			EventItemMapRepository eventItemMapRepository, EventRepository eventRepository) {
		this.memberEventItemRepository = memberEventItemRepository;
		this.memberEventRepository = memberEventRepository;
		this.eventItemRepository = eventItemRepository;
		this.coreUserRepository = coreUserRepository;
		this.nodeRepository = nodeRepository;
		this.eventItemMapRepository = eventItemMapRepository;
		this.eventRepository = eventRepository;
	}

	@Transactional(readOnly = true)
	public Optional<MemberEventItem> findById(Long id) {
		return memberEventItemRepository.findByIdAndNotDeleted(id);
	}

	@Transactional
	public MemberEventItem update(Long id, MemberEventItem updatedItem) {
		return memberEventItemRepository.findByIdAndNotDeleted(id).map(existingItem -> {
			// Update only the fields that should be modified

			// Approval fields should be updated through separate approve method
			return memberEventItemRepository.save(existingItem);
		}).orElseThrow(() -> new ResourceNotFoundException("MemberEventItem not found with id: " + id, id));
	}

	@Transactional
	public MemberEventItem approveItem(Long id, Long approvedByUserId) {
		return memberEventItemRepository.findByIdAndNotDeleted(id).map(item -> {
			item.setApproveDateTime(LocalDateTime.now());
			// Assuming you have a method to set approvedBy with CoreUser
			// item.setApprovedBy(getCoreUserById(approvedByUserId));
			return memberEventItemRepository.save(item);
		}).orElseThrow(() -> new ResourceNotFoundException("MemberEventItem not found with id: " + id, id));
	}

	
	@Transactional
	public void saveParticipation(Long eventId, Long nodeId, Map<String, String> requestParams) {
		
		Long itemId = Long.valueOf(requestParams.get("itemId"));

	    Node branchNode = nodeRepository.findByIdAndNotDeleted(nodeId)
	            .orElseThrow(() -> new ResourceNotFoundException("Host node not found", nodeId));

	    // 1) EXISTING keys from DB  (eventItemId-userId)
	    Set<String> existingKeys = memberEventItemRepository
	            .findKeysByEventAndNode(eventId, nodeId ,itemId); 
	            // implement query:  SELECT CONCAT(m.member_event_map_id,'-',m.member_event_member_id)
	            // FROM MemberEventItem m WHERE m.memberEvent.id=:eventId AND m.memberEventNode.id=:nodeId

	    // 2) SUBMITTED keys + details from request
	    class Submission {
	        final Long eventItemId;
	        final Long userId;
	        final EventItemMap.Category category;
	        final CoreUser.Gender gender;
	        Submission(Long e, Long u, EventItemMap.Category c, CoreUser.Gender g) {
	            this.eventItemId = e; this.userId = u; this.category = c; this.gender = g;
	        }
	    }

	    Map<String, Submission> submitted = new HashMap<>();

	    requestParams.forEach((key, value) -> {
	    	if (!key.startsWith("selectedUsers[")) return;


	        String[] parts = Arrays.stream(key.split("\\[|\\]"))
	                               .filter(s -> !s.isEmpty() && !s.equals("selectedUsers"))
	                               .toArray(String[]::new);

	        Long userId = Long.valueOf(parts[0]);
	        EventItemMap.Category category = EventItemMap.Category.valueOf(parts[1]);
	        CoreUser.Gender gender = CoreUser.Gender.valueOf(parts[2].toUpperCase());
	        Long eventItemId = Long.valueOf(value);

	        String k = eventItemId + "-" + userId;
	        submitted.put(k, new Submission(eventItemId, userId, category, gender));
	    });

	    // 3) Determine changes
	    Set<String> toInsertKeys = new HashSet<>(submitted.keySet());
	    toInsertKeys.removeAll(existingKeys);          // only new

	    Set<String> toDeleteKeys = new HashSet<>(existingKeys);
	    toDeleteKeys.removeAll(submitted.keySet());    // only removed

	    // 4) Delete removed
	    if (!toDeleteKeys.isEmpty()) {
	        memberEventItemRepository.deleteByKeys(toDeleteKeys,itemId);
	        // implement query using keys or (eventItemId,userId) pair
	    }

	    // 5) Insert new
	    for (String k : toInsertKeys) {
	        Submission s = submitted.get(k);

	        CoreUser member = coreUserRepository.findById(s.userId)
	                .orElseThrow(() -> new ResourceNotFoundException("Member not found", s.userId));
	        EventItemMap map = eventItemMapRepository.findById(s.eventItemId)
	                .orElseThrow(() -> new ResourceNotFoundException("EventItemMap not found", s.eventItemId));
	        Event event = map.getEvent();

	        MemberEventItem item = new MemberEventItem();
	        item.setMemberEvent(event);
	        item.setMemberEventMember(member);
	        item.setMemberEventItem(map.getItem());
	        item.setMemberEventNode(branchNode);
	        item.setMemberEventHost(event.getHostNode());
	        item.setMemberEventMap(map);
	        item.setMemberEventCategory(s.category);
	        item.setMemberEventGender(s.gender);
	        item.setMemberEventItemName(map.getItem().getEvitemName());
	        item.setMemberEventScore(0);
	        memberEventItemRepository.save(item);
	    }
	}




	
	@Transactional
	public void saveParticipation1(Long eventId, Long nodeId, Map<String, String> requestParams) {

		// MemberEvent memberEvent = memberEventRepository.findById(eventId)
		// .orElseThrow(() -> new ResourceNotFoundException("MemberEvent not found",
		// eventId));

		Node branchNode = nodeRepository.findByIdAndNotDeleted(nodeId)
				.orElseThrow(() -> new ResourceNotFoundException("Host node not found", nodeId));

		requestParams.forEach((k, v) -> this.debug("Param key=" + k + "value=" + v));

		Set<String> selectedKeys = this.getSelectedKeys(eventId, nodeId);

		Set<String> submittedKeys = new HashSet<>();

		requestParams.forEach((key, value) -> {
			if (!key.startsWith("selectedUsers[")) {
				return; // skip _csrf, pageParams, etc.
			}
			
			// Split and filter empty strings
			String[] parts = Arrays.stream(key.split("\\[|\\]")).filter(s -> !s.isEmpty() && !s.equals("selectedUsers"))
					.toArray(String[]::new);

			// parts[0] -> eventItemId
			// parts[1] -> category
			// parts[2] -> gender
			Long userId = Long.valueOf(parts[0]);
			String categoryRaw = parts[1];
			String genderRaw = parts[2];
			Long eventItemId = Long.valueOf(value);
			
			
			String keyStr = eventItemId + "-" + userId;
			submittedKeys.add(keyStr);

			// Skip if already exists
			if (selectedKeys.contains(keyStr)) {
				this.debug("Skipping duplicate: " + keyStr);
				return;
			}

			this.debug("eventItemId=" + eventItemId + ", category=" + categoryRaw + ", gender=" + genderRaw
					+ ", userId=" + userId);

			EventItemMap.Category category = EventItemMap.Category.valueOf(categoryRaw);
			CoreUser.Gender gender = CoreUser.Gender.valueOf(genderRaw.toUpperCase());

			CoreUser member = coreUserRepository.findById(userId)
					.orElseThrow(() -> new ResourceNotFoundException("MemberEvent not found", userId));
			EventItemMap eventItemMap = eventItemMapRepository.findById(eventItemId)
					.orElseThrow(() -> new ResourceNotFoundException("MemberEvent not found", eventItemId));
			Event event = eventItemMap.getEvent();



			MemberEventItem item = new MemberEventItem();
			item.setMemberEvent(event);
			item.setMemberEventMember(member);
			item.setMemberEventItem(eventItemMap.getItem());
			item.setMemberEventNode(branchNode);
			item.setMemberEventHost(eventItemMap.getEvent().getHostNode());
			item.setMemberEventMap(eventItemMap);
			item.setMemberEventCategory(category);
			item.setMemberEventGender(gender);
			item.setMemberEventItemName(eventItemMap.getItem().getEvitemName());
			item.setMemberEventScore(0);
			item.setMemberEventGrade(null);
			item.setScoreEntryBy(null);
			item.setApproveDateTime(null);
			item.setApprovedBy(null);

			memberEventItemRepository.save(item);

		});
		

		// selectedKeys = from DB
		// submittedKeys = from UI (can be null/empty)
		Set<String> toDelete = new HashSet<>();
		if (selectedKeys != null && !selectedKeys.isEmpty()) {
		    if (submittedKeys == null) {
		        // nothing submitted → delete everything that exists
		        toDelete.addAll(selectedKeys);
		    } else {
		        // delete only those that were previously selected but not re-submitted
		        toDelete.addAll(selectedKeys);
		        toDelete.removeAll(submittedKeys);
		    }
		}

		// nothing to delete → exit early
		if (toDelete.isEmpty()) {
		    return;
		}

		// remove each record, skip any malformed key
		for (String keyStr : toDelete) {
		    if (keyStr == null || keyStr.isBlank() || !keyStr.contains("-")) {
		        continue; // ignore bad data instead of throwing
		    }
		    try {
		        String[] parts = keyStr.split("-");
		        if (parts.length != 2) continue;

		        Long eventItemId = Long.parseLong(parts[0]);
		        Long userId      = Long.parseLong(parts[1]);

		        memberEventItemRepository.deleteByEventItemAndUserAndEventAndNode(
		                eventItemId, userId, eventId, nodeId);
		    } catch (NumberFormatException ex) {
		        // log and skip bad key
		        // logger.warn("Invalid key: {}", keyStr, ex);
		    }
		}

		
		
		
	}

	private void debug(String message) {
		System.out.println("[DEBUG] " + message);
	}

	public Set<String> getSelectedKeys(Long eventId, Long nodeId) {
		// Fetch existing items
		List<MemberEventItem> existingItems = memberEventItemRepository.findByEventAndNode(eventId, nodeId);

		// Explicitly declare the type in the lambda to help compiler
		Set<String> selectedKeys = existingItems.stream()
				.<String>map(item -> item.getMemberEventMap().getEimId().toString() + "-"
						+ item.getMemberEventMember().getUserId().toString())
				.collect(Collectors.toSet());

		return selectedKeys;
	}
	
	
	
	public Map<String, Map<String, Map<String, List<MemberEventItem>>>> getParticipationMatrix(
	        Long eventId,
	        Long itemId,
	        CoreUser.Gender fGender,
	        EventItemMap.Category fCategory) {

	    List<MemberEventItem> items = memberEventItemRepository
	            .findByEventIdWithFilters(eventId, itemId, fGender, fCategory);

        Map<String, Map<String, Map<String, List<MemberEventItem>>>> matrix = new LinkedHashMap<>();

        for (MemberEventItem mei : items) {
            String itemName = mei.getMemberEventItem().getEvitemName();
            String gender = mei.getMemberEventGender().name();
            String category = mei.getMemberEventCategory().name();

            matrix
                .computeIfAbsent(itemName, k -> new LinkedHashMap<>())
                .computeIfAbsent(gender, k -> new LinkedHashMap<>())
                .computeIfAbsent(category, k -> new ArrayList<>())
                .add(mei);
        }

        return matrix;
    }
	
	
	@Transactional
	public void updateScores(Map<Long, Integer> scores,
	                         Map<Long, MemberEventItem.Grade> grades) {

	    // Collect all IDs to fetch at once
	    Set<Long> meiIds = scores.keySet();

	    // Fetch all items in one query
	    List<MemberEventItem> allMei = memberEventItemRepository.findAllById(meiIds);

	    List<MemberEventItem> toUpdate = new ArrayList<>();

	    for (MemberEventItem mei : allMei) {
	        Long meiId = mei.getMeiId();
	        boolean changed = false;

	        Integer newScore = scores.get(meiId);
	        if (!Objects.equals(mei.getMemberEventScore(), newScore)) {
	            mei.setMemberEventScore(newScore);
	            changed = true;
	        }

	        if (grades.containsKey(meiId)) {
	            MemberEventItem.Grade newGrade = grades.get(meiId);
	            if (!Objects.equals(mei.getMemberEventGrade(), newGrade)) {
	                mei.setMemberEventGrade(newGrade);
	                changed = true;
	            }
	        }

	        if (changed) {
	            toUpdate.add(mei);
	        }
	    }

	    if (!toUpdate.isEmpty()) {
	        memberEventItemRepository.saveAll(toUpdate);
	    }
	}



}