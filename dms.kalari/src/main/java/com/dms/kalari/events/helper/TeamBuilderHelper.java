package com.dms.kalari.events.helper;

import com.dms.kalari.admin.entity.CoreUser;
import com.dms.kalari.events.dto.TeamOptionDTO;
import com.dms.kalari.events.entity.EventItem;
import com.dms.kalari.events.entity.EventItemMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamBuilderHelper {

    public static Map<String, List<TeamOptionDTO>> buildTeamOptions(

            Map<Long, Map<String, List<CoreUser>>> memberMatrix,

            List<EventItemMap> allItems
    ) {

        Map<String, List<TeamOptionDTO>> result =
                new HashMap<>();

        for (EventItemMap eim : allItems) {

            // ==========================================
            // VALIDATION
            // ==========================================

            if (eim == null || eim.getItem() == null) {

                continue;
            }

            EventItem item =
                    eim.getItem();

            // ==========================================
            // TEAM ITEMS ONLY
            // ==========================================

            if (item.getEvitemType()
                    != EventItem.EventItemType.T) {

                continue;
            }

            // ==========================================
            // MATRIX FOR THIS EIM
            // ==========================================

            Map<String, List<CoreUser>> genderMap =
                    memberMatrix.get(
                            eim.getEimId()
                    );

            if (genderMap == null
                    || genderMap.isEmpty()) {

                continue;
            }

            // ==========================================
            // GENDER LOOP
            // ==========================================

            for (Map.Entry<String, List<CoreUser>> genderEntry
                    : genderMap.entrySet()) {

                String gender =
                        genderEntry.getKey();

                List<CoreUser> members =
                        genderEntry.getValue();

                if (members == null
                        || members.isEmpty()) {

                    continue;
                }

                // ==========================================
                // TEAM COUNT
                // ==========================================

                int participantCount =
                        members.size();

                int totalTeams =
                        (int) Math.ceil(
                                participantCount / 2.0
                        ) + 2;

                List<TeamOptionDTO> teams =
                        new ArrayList<>();

                for (int i = 1; i <= totalTeams; i++) {

                    String teamCode =
                            item.getEvitemCode()
                                    + "-T"
                                    + i;

                    teams.add(
                            new TeamOptionDTO(
                                    teamCode,
                                    0,
                                    2
                            )
                    );
                }

                // ==========================================
                // TEMPLATE KEY
                // ==========================================

                String key =
                        item.getEvitemCode()
                                + "-"
                                + eim.getCategory().name()
                                + "-"
                                + gender;

                result.put(
                        key,
                        teams
                );
            }
        }

        return result;
    }
}