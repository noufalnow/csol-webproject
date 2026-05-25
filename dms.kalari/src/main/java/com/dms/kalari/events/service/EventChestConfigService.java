package com.dms.kalari.events.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
                eventItemMapRepository.findByEvent_EventIdOrderByItem_EvitemNameAsc(eventId);

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
    public void regenerateChestNumbers(
            Long eventId,
            boolean regenerate
    ) {

        List<EventChestConfig> configs =
                eventChestConfigRepository.findByEventId(eventId);

        for (EventChestConfig config : configs) {

            if (config.getStartNo() == null) {
                continue;
            }

            List<MemberEventItem> members =
                    memberEventItemRepository
                            .findAllByEventItemAndGender(
                                    config.getEventItemMap().getEimId(),
                                    config.getGender()
                            );

            if (members.isEmpty()) {
                continue;
            }

            /*
             * REORDER GENERATION:
             * true when regenerate is forced
             * OR nobody has a chest number yet.
             * controls:
             *   → team→name ordering
             *   → currentNo starts from startNo
             */
            boolean reorderGeneration =

                    regenerate

                    ||

                    members.stream()
                            .noneMatch(
                                    m ->
                                            m.getMemberChestNo()
                                                    != null
                            );

            /*
             * FETCH ORDERED LIST
             * TEAM → NAME
             */
            if (reorderGeneration) {

                members =
                        memberEventItemRepository
                                .findAllByEventItemAndGenderOrderTeamAndName(
                                        config.getEventItemMap().getEimId(),
                                        config.getGender()
                                );
            }

            /*
             * REGENERATE MODE:
             * CLEAR CHEST NUMBERS IN MEMORY
             * FOR ALL UNSCORED MEMBERS
             *
             * WHY:
             * chest numbers are not cleared in DB here.
             * without this, existing unscored members
             * retain their old chestNo in memory,
             * so downstream == null filters skip them.
             * clearing in memory lets all downstream
             * logic treat them as blank.
             * inheritance re-assigns team members correctly.
             * new numbers assigned to the rest.
             */
            if (regenerate) {

                members.stream()

                        .filter(
                                m -> !hasScores(m)
                        )

                        .forEach(
                                m ->
                                        m.setMemberChestNo(
                                                null
                                        )
                        );
            }

            /*
             * RESERVE EXISTING CHEST NUMBERS
             * regenerate=false → reserve all assigned
             * regenerate=true  → reserve scored only
             */
            Set<Long> usedNumbers =

                    members.stream()

                            .filter(
                                    m ->

                                            !regenerate

                                            ||

                                            hasScores(m)
                            )

                            .map(
                                    MemberEventItem
                                            ::getMemberChestNo
                            )

                            .filter(
                                    Objects::nonNull
                            )

                            .collect(
                                    Collectors.toSet()
                            );

            Long currentNo;

            if (reorderGeneration) {

                currentNo =
                        config.getStartNo();

            } else {

                currentNo =

                        usedNumbers.stream()

                                .max(
                                        Long::compare
                                )

                                .orElse(
                                        config.getStartNo()
                                                - 1
                                )

                                + 1;
            }

            /*
             * USING LinkedHashSet TO PREVENT
             * DUPLICATE SAVES
             * preserves insertion order
             */
            Set<MemberEventItem> toUpdate =
                    new LinkedHashSet<>();

            /*
             * INHERIT TEAM CHEST
             * FOR REPLACED / REGENERATED TEAM MEMBERS
             *
             * regenerate=false → inherit from any assigned member
             * regenerate=true  → inherit from scored members only
             *   (unscored were cleared above,
             *    cannot serve as inheritance source)
             */
            Map<String, Long> existingTeamChestMap =

                    members.stream()

                            .filter(
                                    m ->

                                            m.getMemberChestNo()
                                                    != null

                                            &&

                                            (
                                                    !regenerate

                                                    ||

                                                    hasScores(m)
                                            )
                            )

                            .filter(
                                    m ->

                                            m.getMemberEventTeamCode()
                                                    != null

                                                    &&

                                                    !m.getMemberEventTeamCode()
                                                            .trim()
                                                            .isEmpty()
                            )

                            .collect(

                                    Collectors.toMap(

                                            m ->

                                                    m.getMemberEventMember()
                                                            .getUserNode()
                                                            .getNodeId()

                                                            + "-"

                                                            +

                                                            m.getMemberEventTeamCode()
                                                                    .trim(),

                                            MemberEventItem
                                                    ::getMemberChestNo,

                                            (a, b) -> a

                                    )

                            );

            /*
             * APPLY TEAM INHERITANCE
             * skip members who already have a number
             * (scored members, previously inherited)
             */
            for (MemberEventItem member : members) {

                if (
                        member.getMemberChestNo()
                                != null
                ) {
                    continue;
                }

                String teamCode =
                        member.getMemberEventTeamCode();

                if (
                        teamCode == null

                        ||

                        teamCode.trim()
                                .isEmpty()
                ) {
                    continue;
                }

                String key =

                        member.getMemberEventMember()
                                .getUserNode()
                                .getNodeId()

                                + "-"

                                +

                                teamCode.trim();

                Long inherited =
                        existingTeamChestMap.get(key);

                if (inherited != null) {

                    member.setMemberChestNo(
                            inherited
                    );

                    /*
                     * reserve inherited number
                     * so new assignments don't collide
                     */
                    usedNumbers.add(
                            inherited
                    );

                    toUpdate.add(
                            member
                    );
                }
            }

            /*
             * NEW TEAMS
             * chestNo == null because:
             *   unscored existing → cleared above
             *   inherited → set above, not null
             *   scored → never cleared, not null
             */
            Map<String,
                    List<MemberEventItem>>

                    groupedTeams =

                    members.stream()

                            .filter(
                                    m ->
                                            m.getMemberChestNo()
                                                    == null
                            )

                            .filter(
                                    m ->

                                            m.getMemberEventTeamCode()
                                                    != null

                                                    &&

                                                    !m.getMemberEventTeamCode()
                                                            .trim()
                                                            .isEmpty()
                            )

                            .collect(

                                    Collectors.groupingBy(

                                            m ->

                                                    m.getMemberEventMember()
                                                            .getUserNode()
                                                            .getNodeId()

                                                            + "-"

                                                            +

                                                            m.getMemberEventTeamCode()
                                                                    .trim(),

                                            LinkedHashMap::new,

                                            Collectors.toList()

                                    )

                            );

            /*
             * ASSIGN ONE NUMBER PER TEAM
             */
            for (

                    Entry<String,
                            List<MemberEventItem>>

                            entry

                    : groupedTeams.entrySet()

            ) {

                while (
                        usedNumbers.contains(
                                currentNo
                        )
                ) {
                    currentNo++;
                }

                Long teamChestNo =
                        currentNo;

                for (
                        MemberEventItem member
                        : entry.getValue()
                ) {

                    member.setMemberChestNo(
                            teamChestNo
                    );

                    toUpdate.add(
                            member
                    );
                }

                usedNumbers.add(
                        teamChestNo
                );

                currentNo++;
            }

            /*
             * INDIVIDUAL MEMBERS
             * chestNo == null sufficient
             * after memory clear above
             */
            List<MemberEventItem> individualMembers =

                    members.stream()

                            .filter(
                                    m ->
                                            m.getMemberChestNo()
                                                    == null
                            )

                            .filter(
                                    m ->

                                            m.getMemberEventTeamCode()
                                                    == null

                                                    ||

                                                    m.getMemberEventTeamCode()
                                                            .trim()
                                                            .isEmpty()
                            )

                            .toList();

            for (
                    MemberEventItem member
                    : individualMembers
            ) {

                while (
                        usedNumbers.contains(
                                currentNo
                        )
                ) {
                    currentNo++;
                }

                member.setMemberChestNo(
                        currentNo
                );

                usedNumbers.add(
                        currentNo
                );

                toUpdate.add(
                        member
                );

                currentNo++;
            }

            if (!toUpdate.isEmpty()) {

                memberEventItemRepository
                        .saveAll(
                                new ArrayList<>(toUpdate)
                        );
            }
        }
    }

    private boolean hasScores(MemberEventItem member) {

        return

            (member.getMemberEventScore() != null
                    && member.getMemberEventScore() > 0)

            ||

            (member.getMemberScore1() != null
                    && member.getMemberScore1() > 0)

            ||

            (member.getMemberScore2() != null
                    && member.getMemberScore2() > 0)

            ||

            (member.getMemberScore3() != null
                    && member.getMemberScore3() > 0)
        
            ||

            (member.getMemberEventGrade() != null);
        
        
    }
    
    
    private boolean eligibleForGeneration(
	        MemberEventItem member,
	        boolean regenerate
	) {

	    if (hasScores(member)) {
	        return false;
	    }

	    if (regenerate) {
	        return true;
	    }

	    return member.getMemberChestNo() == null;
	}
    
    
    
    
    public Map<String, EventChestConfig> getJudgeConfigsByEvent(Long eventId) {

	    List<EventChestConfig> configs =
	            eventChestConfigRepository.findByEventId(eventId);

	    Map<String, EventChestConfig> map = new HashMap<>();

	    for (EventChestConfig config : configs) {

	        String key =
	                config.getEventItemMap().getEimId()
	                + "_"
	                + config.getGender().name();

	        map.put(key, config);
	    }

	    return map;
	}
    
    
    @Transactional
    public void updateJudgeNames(
            Map<String, String> judge1Map,
            Map<String, String> judge2Map,
            Map<String, String> judge3Map) {

        Set<String> keys = new HashSet<>();

        keys.addAll(judge1Map.keySet());
        keys.addAll(judge2Map.keySet());
        keys.addAll(judge3Map.keySet());

        if (keys.isEmpty()) {
            return;
        }

        List<EventChestConfig> toUpdate =
                new ArrayList<>();

        for (String key : keys) {

            String[] parts = key.split("_");

            Long eimId =
                    Long.valueOf(parts[0]);

            CoreUser.Gender gender =
                    CoreUser.Gender.valueOf(parts[1]);

            Optional<EventChestConfig> optional =
                    repository.findByEimIdAndGender(
                            eimId,
                            gender
                    );

            if (optional.isEmpty()) {
                continue;
            }

            EventChestConfig config =
                    optional.get();

            config.setJudge1(
                    judge1Map.get(key));

            config.setJudge2(
                    judge2Map.get(key));

            config.setJudge3(
                    judge3Map.get(key));

            toUpdate.add(config);
        }

        if (!toUpdate.isEmpty()) {
            repository.saveAll(toUpdate);
        }
    }
}