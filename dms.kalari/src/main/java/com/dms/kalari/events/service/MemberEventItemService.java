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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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

		// MemberEvent memberEvent = memberEventRepository.findById(eventId)
		// .orElseThrow(() -> new ResourceNotFoundException("MemberEvent not found",
		// eventId));

		Node branchNode = nodeRepository.findByIdAndNotDeleted(nodeId)
				.orElseThrow(() -> new ResourceNotFoundException("Host node not found", nodeId));

		requestParams.forEach((k, v) -> this.debug("Param key=" + k + "value=" + v));

		Set<String> selectedKeys = this.getSelectedKeys(eventId, nodeId);

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

}