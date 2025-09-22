package com.dms.kalari.events.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;

import com.dms.kalari.common.BaseDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MemberEventDTO extends BaseDTO {
	private Long meId;
    private Long memberId;
    private Long memberNodeId;
    private Long eventId;
    private Long nodeId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime applyDate;
    public Map<Integer,String> items = new HashMap<>();
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime approvedDate;
    private Long approvedBy;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime resultDate;
    private Long resultEntryBy;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime resultApprovalDate;
    private Long resultApprovedBy;
    
    private String memberName;  // from CoreUsers
    private String memberNodeName;  // from Node
    
    /*public enum KalariItem {
        CHUVADU(1, "CHUVADU"),
        HIGHKICK_CHAVITTIPONGHAL(2, "HIGH KICK(CHAVITTIPONGHAL)"),
        MEYPPAYATTU(3, "MEYPPAYATTU"),
        WIELDINGOF_URUMI(4, "WIELDINGOF URUMI (FLEXIBLE SWORD)"),
        SWORD_AND_SWORD(5, "SWORD & SWORD (Seniors only)"),
        SWORD_AND_SHIELD(6, "SWORD & SHIELD"),
        URUMI_AND_SHIELD(7, "URUMI & SHIELD"),
        LONG_STAFF_FIGHT(8, "LONG STAFF FIGHT"),
        KURUVADI(9, "KURUVADI (SHORT STAFF FIGHT)"),
        KAIPPORU(10, "KAIPPORU/UNARMED COMBAT (Seniors only)");

        private final int id;
        private final String displayName;

        KalariItem(int id, String displayName) {
            this.id = id;
            this.displayName = displayName;
        }

        public int getId() {
            return id;
        }

        public String getDisplayName() {
            return displayName;
        }

        public static KalariItem fromId(int id) {
            for (KalariItem item : values()) {
                if (item.id == id) {
                    return item;
                }
            }
            throw new IllegalArgumentException("Invalid KalariItem ID: " + id);
        }
    }*/
    
}