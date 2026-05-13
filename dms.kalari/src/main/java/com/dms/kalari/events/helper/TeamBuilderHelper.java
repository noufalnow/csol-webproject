package com.dms.kalari.events.helper;
import com.dms.kalari.admin.entity.CoreUser;
import com.dms.kalari.events.entity.EventItem;
import com.dms.kalari.events.entity.EventItemMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.dms.kalari.events.dto.TeamOptionDTO;

public class TeamBuilderHelper {

    public static Map<String, List<TeamOptionDTO>> buildTeamOptions(
            Map<String, Map<String, List<CoreUser>>> memberMatrix,
            List<EventItemMap> allItems
    ) {

        Map<String, List<TeamOptionDTO>> result = new HashMap<>();

        for (EventItemMap eim : allItems) {

            if (eim == null || eim.getItem() == null) {
                continue;
            }

            EventItem item = eim.getItem();

            // ONLY TEAM ITEMS
            if (item.getEvitemType() != EventItem.EventItemType.T) {
                continue;
            }

            String category = eim.getCategory().name();

            // category not found in matrix
            if (!memberMatrix.containsKey(category)) {
                continue;
            }

            Map<String, List<CoreUser>> genderMap =
                    memberMatrix.get(category);

            for (Map.Entry<String, List<CoreUser>> genderEntry
                    : genderMap.entrySet()) {

                String gender = genderEntry.getKey();

                List<CoreUser> members = genderEntry.getValue();

                if (members == null || members.isEmpty()) {
                    continue;
                }

                int participantCount = members.size();

                int totalTeams =
                        (int) Math.ceil(participantCount / 2.0);

                List<TeamOptionDTO> teams =
                        new ArrayList<>();

                for (int i = 1; i <= totalTeams; i++) {

                    String teamCode =
                            item.getEvitemCode() + "-T" + i;

                    teams.add(
                            new TeamOptionDTO(
                                    teamCode,
                                    0,
                                    2
                            )
                    );
                }

                /*
                 * UNIQUE TEMPLATE KEY
                 *
                 * MY-SENIOR-MALE
                 */
                String key =
                        item.getEvitemCode()
                        + "-"
                        + category
                        + "-"
                        + gender;

                result.put(key, teams);
            }
        }

        return result;
    }
}