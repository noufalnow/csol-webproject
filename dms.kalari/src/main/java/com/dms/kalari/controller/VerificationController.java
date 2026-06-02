package com.dms.kalari.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.dms.kalari.admin.entity.CoreUser;
import com.dms.kalari.admin.entity.CoreUser.UserType;
import com.dms.kalari.admin.repository.CoreUserRepository;
import com.dms.kalari.branch.entity.Node;
import com.dms.kalari.branch.repository.NodeRepository;
import com.dms.kalari.events.entity.MemberEventItem;
import com.dms.kalari.events.repository.MemberEventItemRepository;
import com.dms.kalari.events.service.EventService;
import com.dms.kalari.util.SimpleBase64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/verify")
public class VerificationController {

    private static final Logger logger = LoggerFactory.getLogger(VerificationController.class);
    private final CoreUserRepository coreUserRepository;
    private final NodeRepository nodeRepository;
    private final MemberEventItemRepository memberEventItemRepository;
    
    @Value("${app.host.url-path}")
    private String HOST_URL;
    
    

    private Long decodeVerificationId(String encodedId) {

	try {

	    String decoded = SimpleBase64.decode(encodedId);

	    String[] parts = decoded.split("\\|", 2);

	    return Long.parseLong(parts[0]);

	} catch (Exception ex) {

	    throw new IllegalArgumentException("Invalid verification code", ex);
	}
    }

    public record DecodedCertificate(Long meiId, Long itemId) {
    }

    private final EventService eventService;

    public VerificationController(EventService eventService, NodeRepository nodeRepository,
	    CoreUserRepository coreUserRepository, MemberEventItemRepository memberEventItemRepository) {
	this.eventService = eventService;
	this.coreUserRepository = coreUserRepository;
	this.nodeRepository = nodeRepository;
	this.memberEventItemRepository = memberEventItemRepository;
    }

    @GetMapping
    public String verifyCertificate(@RequestParam String id, Model model) {

	try {

	    Long meiId = decodeVerificationId(id);

	    MemberEventItem mei = memberEventItemRepository.findById(meiId)
		    .orElseThrow(() -> new IllegalArgumentException("Certificate not found"));

	    Map<String, Object> data = prepareVerificationData(mei);

	    model.addAttribute("verification", data);

	    model.addAttribute("success", true);
	    
	    model.addAttribute("HOST_URL", HOST_URL);

	    return "fragments/manage/members/profile/verification-result";

	} catch (Exception e) {

	    return handleError(model, "Invalid verification code", e);
	}
    }

    private Map<String, Object> prepareVerificationData(MemberEventItem mei) {

	    Map<String, Object> data = new HashMap<>();

	    Node eventNode = mei.getMemberEventHost();
	    CoreUser participant = mei.getMemberEventMember();

	    // PARTICIPANT
	    data.put("participantId",      participant.getUserId());
	    data.put("participantName",    (participant.getUserFname() + " " + participant.getUserLname()).trim());
	    data.put("participantPhotoId", participant.getPhotoFile());
	    data.put("gender",             mei.getMemberEventGender());
	    data.put("category",           mei.getMemberEventCategory());
	    data.put("chestNo",            mei.getMemberChestNo());
	    data.put("representing",       participant.getUserFname());

	    // CERTIFICATE
	    data.put("certificateNo",  mei.getMeiCertificateNo());
	    data.put("verificationId", mei.getMeiId());
	    data.put("approvedDate",   mei.getApproveDateTime());

	    // EVENT
	    data.put("eventName",  mei.getMemberEvent().getEventName());
	    data.put("eventYear",  mei.getMemberEvent().getEventYear());
	    data.put("eventStart", mei.getMemberEvent().getEventPeriodStart());
	    data.put("eventEnd",   mei.getMemberEvent().getEventPeriodEnd());
	    data.put("itemName",   mei.getMemberEventItemName());
	    data.put("grade",      mei.getMemberEventGrade());

	    // HOST
	    data.put("hostNodeName", eventNode.getNodeName());
	    data.put("hostNodeType", eventNode.getNodeType());

	    // HIERARCHY — resolve district / state / national nodes
	    Node district = null, state = null, national = null;

	    switch (eventNode.getNodeType()) {
	        case DISTRICT -> {
	            district = eventNode;
	            if (district.getParent() != null) {
	                state = nodeRepository.findByIdAndNotDeleted(district.getParent().getNodeId()).orElse(null);
	            }
	            if (state != null && state.getParent() != null) {
	                national = nodeRepository.findByIdAndNotDeleted(state.getParent().getNodeId()).orElse(null);
	            }
	        }
	        case STATE -> {
	            state = eventNode;
	            if (state.getParent() != null) {
	                national = nodeRepository.findByIdAndNotDeleted(state.getParent().getNodeId()).orElse(null);
	            }
	        }
	        case NATIONAL -> national = eventNode;
	    }

	    // HIERARCHY VIEW
	    data.put("districtName",    district != null ? district.getNodeName()  : null);
	    data.put("stateName",       state    != null ? state.getNodeName()     : null);
	    data.put("nationalName",    national != null ? national.getNodeName()  : null);

	    Long districtLogoId = district != null ? district.getPhotoFile() : null;
	    Long stateLogoId    = state    != null ? state.getPhotoFile()    : null;
	    Long nationalLogoId = national != null ? national.getPhotoFile() : null;

	    // District logo falls back to state logo if missing
	    Long districtLogoIdEffective = districtLogoId != null ? districtLogoId
	                                 : stateLogoId    != null ? stateLogoId
	                                 : null;

	    data.put("districtLogoId", districtLogoId);
	    data.put("stateLogoId",    stateLogoId);
	    data.put("nationalLogoId", nationalLogoId);

	    // AUTHORITY (header)
	    data.put("authorityLogoId", districtLogoIdEffective != null ? districtLogoIdEffective
	                               : stateLogoId           != null  ? stateLogoId
	                               : nationalLogoId);
	    data.put("authorityName",   eventNode.getNodeName());
	    data.put("authorityType",   eventNode.getNodeType().name());

	    // HEADER
	    data.put("branchTagline", eventNode.getBranchCertTagLine());
	    data.put("venue",         eventNode.getAddressLine3());

	    // SIGNATORIES
	    List<Map<String, Object>> signatories = coreUserRepository
	        .findBranchSignatories(eventNode.getNodeId(), UserType.OFFICIAL)
	        .stream()
	        .map(u -> {
	            Map<String, Object> s = new HashMap<>();
	            s.put("name",        ((u.getUserFname() != null ? u.getUserFname() : "")
	                                + (u.getUserLname() != null ? " " + u.getUserLname() : "")).trim());
	            s.put("designation", u.getDesignation() != null ? u.getDesignation().getDesigName() : null);
	            s.put("email",       u.getUserEmail());
	            s.put("phone",       u.getMobileNumber());
	            s.put("description", u.getOfficialDescription());
	            return s;
	        }).toList();

	    data.put("signatories", signatories);

	    return data;
	}

    private void logResults(List<Object[]> results) {
	if (logger.isDebugEnabled()) {
	    logger.debug("Verification results:");
	    for (int i = 0; i < results.size(); i++) {
		Object[] row = results.get(i);
		logger.debug("Row {}: {}", i, row != null ? Arrays.toString(row) : "null");
	    }
	}
    }

    private String handleError(Model model, String message, Exception e) {
	logger.error(message, e);
	model.addAttribute("error", message);
	return "verification-error";
    }
}