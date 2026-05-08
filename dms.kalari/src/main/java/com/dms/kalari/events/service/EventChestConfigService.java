package com.dms.kalari.events.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dms.kalari.admin.entity.CoreUser;
import com.dms.kalari.events.dto.EventChestConfigDTO;
import com.dms.kalari.events.entity.EventChestConfig;
import com.dms.kalari.events.entity.EventItemMap;
import com.dms.kalari.events.entity.MemberEventItem;
import com.dms.kalari.events.mapper.EventChestConfigMapper;
import com.dms.kalari.events.repository.EventChestConfigRepository;
import com.dms.kalari.events.repository.EventItemMapRepository;
import com.dms.kalari.events.repository.MemberEventItemRepository;

@Service
public class EventChestConfigService {

    private final EventChestConfigRepository eventChestConfigRepository;

    private final MemberEventItemRepository memberEventItemRepository;

    private final EventChestConfigRepository repository;

    private final EventChestConfigMapper mapper;

    private final EventItemMapRepository eventItemMapRepository;

    @Autowired
    public EventChestConfigService(
            EventChestConfigRepository eventChestConfigRepository,
            MemberEventItemRepository memberEventItemRepository,
            EventChestConfigMapper mapper,
            EventItemMapRepository eventItemMapRepository
    ) {

        this.eventChestConfigRepository = eventChestConfigRepository;
        this.memberEventItemRepository = memberEventItemRepository;

        this.repository = eventChestConfigRepository;

        this.mapper = mapper;
        this.eventItemMapRepository = eventItemMapRepository;
    }

    /*
     * MATRIX LOAD
     */
    public Map<String, Map<String, Map<String, EventChestConfigDTO>>>
    getChestConfigMatrix(Long eventId) {

        /*
         * EVENT ITEM MAPS
         */
        List<EventItemMap> itemMaps =
                eventItemMapRepository.findByEvent_EventId(eventId);

        /*
         * EXISTING CONFIGS
         */
        List<EventChestConfig> configs =
                eventChestConfigRepository.findByEventId(eventId);

        /*
         * CONFIG LOOKUP
         */
        Map<String, EventChestConfig> configMap =
                new HashMap<>();

        for (EventChestConfig cfg : configs) {

            String key =
                    cfg.getEventItemMap().getEimId()
                    + "_"
                    + cfg.getGender().name();

            configMap.put(key, cfg);
        }

        /*
         * FINAL MATRIX
         *
         * ITEM
         *   -> CATEGORY
         *        -> GENDER
         *              -> DTO
         */
        Map<String,
            Map<String,
                Map<String, EventChestConfigDTO>>> matrix =
                new LinkedHashMap<>();

        for (EventItemMap eim : itemMaps) {

            String itemName =
                    eim.getItem().getEvitemName();

            String category =
                    eim.getCategory().name();

            for (CoreUser.Gender gender :
                    CoreUser.Gender.values()) {

                String configKey =
                        eim.getEimId()
                        + "_"
                        + gender.name();

                EventChestConfig existing =
                        configMap.get(configKey);

                EventChestConfigDTO dto =
                        new EventChestConfigDTO();

                dto.setChestConfigId(
                        existing != null
                                ? existing.getChestConfigId()
                                : null);

                dto.setEventItemMapId(
                        eim.getEimId());

                dto.setEventId(eventId);

                dto.setItemId(
                        eim.getItem().getEvitemId());

                dto.setItemName(itemName);

                dto.setCategory(
                        eim.getCategory());

                dto.setGender(gender);

                if (existing != null) {

                    dto.setStartNo(
                            existing.getStartNo());

                    dto.setCurrentNo(
                            existing.getCurrentNo());

                    dto.setPrefix(
                            existing.getPrefix());

                    dto.setSuffix(
                            existing.getSuffix());

                } else {

                    dto.setStartNo(null);

                    dto.setCurrentNo(null);

                    dto.setPrefix("");

                    dto.setSuffix("");
                }

                matrix
                    .computeIfAbsent(
                            itemName,
                            k -> new LinkedHashMap<>())

                    .computeIfAbsent(
                            category,
                            k -> new LinkedHashMap<>())

                    .put(
                            gender.name(),
                            dto);
            }
        }

        return matrix;
    }

    /*
     * GRID SAVE
     */
    @Transactional
    public void updateChestConfigs(
            Long eventId,
            List<EventChestConfigDTO> rows
    ) {

        if (rows == null || rows.isEmpty()) {
            return;
        }

        // incoming eim ids
        List<Long> eimIds = rows.stream()
                .map(EventChestConfigDTO::getEventItemMapId)
                .distinct()
                .toList();

        // remove stale configs
        repository.deleteMissingConfigs(
                eventId,
                eimIds
        );

        List<EventChestConfig> toSave =
                new ArrayList<>();

        for (EventChestConfigDTO dto : rows) {

            Optional<EventChestConfig> optional =
                    repository.findByEimIdAndGender(
                            dto.getEventItemMapId(),
                            dto.getGender()
                    );

            EventChestConfig entity;

            /*
             * UPDATE
             */
            if (optional.isPresent()) {

                entity = optional.get();
            }

            /*
             * INSERT
             */
            else {

                entity = new EventChestConfig();

                EventItemMap eim =
                        eventItemMapRepository
                                .findById(dto.getEventItemMapId())
                                .orElseThrow();

                entity.setEventItemMap(eim);

                entity.setGender(dto.getGender());

                entity.setStartNo(dto.getCurrentNo());

                entity.setCurrentNo(dto.getCurrentNo());
            }

            entity.setStartNo(dto.getCurrentNo());

            entity.setCurrentNo(dto.getCurrentNo());

            toSave.add(entity);
        }

        repository.saveAll(toSave);
    }

    @Transactional
    public void regenerateChestNumbers(Long eventId) {

	    System.out.printf("\n===== REGENERATE CHEST NUMBERS : EVENT %d =====\n", eventId);

	    List<EventChestConfig> configs =
	            eventChestConfigRepository.findByEventId(eventId);

	    System.out.printf("Total Configs Found : %d\n", configs.size());

	    for (EventChestConfig config : configs) {

	        System.out.printf(
	                "\nProcessing Config -> EIM ID : %d | Gender : %s\n",
	                config.getEventItemMap().getEimId(),
	                config.getGender()
	        );

	        if (config.getStartNo() == null) {

	            System.out.printf(
	                    "Skipped : Start Number is NULL for Config ID %d\n",
	                    config.getChestConfigId()
	            );

	            continue;
	        }

	        Long currentNo = config.getStartNo();

	        System.out.printf(
	                "Starting Chest Number : %d\n",
	                currentNo
	        );

	        List<MemberEventItem> members =
	                memberEventItemRepository
	                        .findAllByEventItemAndGender(
	                                config.getEventItemMap().getEimId(),
	                                config.getGender()
	                        );

	        System.out.printf(
	                "Members Found : %d\n",
	                members.size()
	        );

	        for (MemberEventItem member : members) {

	            System.out.printf(
	                    "Assigning Chest No %d -> MEI ID : %d | Member : %s\n",
	                    currentNo,
	                    member.getMeiId(),
	                    member.getMemberEventMember() != null
	                            ? member.getMemberEventMember().getUserFname()
	                            : "UNKNOWN"
	            );

	            member.setMemberChestNo(currentNo);

	            currentNo++;
	        }

	        //config.setCurrentNo(currentNo);

	        System.out.printf(
	                "Next Available Chest Number Saved : %d\n",
	                currentNo
	        );

	        memberEventItemRepository.saveAll(members);

	        //eventChestConfigRepository.save(config);

	        System.out.printf(
	                "Config Saved Successfully : %d\n",
	                config.getChestConfigId()
	        );
	    }

	    System.out.printf(
	            "\n===== CHEST NUMBER REGENERATION COMPLETED =====\n"
	    );
	}
}