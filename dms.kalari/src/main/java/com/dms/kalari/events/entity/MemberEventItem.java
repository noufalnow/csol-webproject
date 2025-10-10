package com.dms.kalari.events.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.DynamicUpdate;

import com.dms.kalari.admin.entity.CoreUser;
import com.dms.kalari.branch.entity.Node;
import com.dms.kalari.common.BaseEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@DynamicUpdate
@Table(
	    name = "member_events_items",
	    uniqueConstraints = @UniqueConstraint(
	        name = "uq_member_events_items_event_member_item",
	        columnNames = {"mei_event_id", "mei_member_id", "mei_item_id"}
	    )
	)
public class MemberEventItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mei_id")
    private Long meiId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mei_event_id", nullable = false)
    private Event memberEvent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mei_member_node", nullable = false)
    private Node memberEventNode;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mei_member_id", nullable = false)
    private CoreUser memberEventMember;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mei_item_id", nullable = false)
    private EventItem memberEventItem;
    
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mei_eim_id", nullable = false)
    private EventItemMap memberEventMap;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mei_event_host", nullable = false)
    private Node memberEventHost;

    @Enumerated(EnumType.STRING)
    @Column(name = "mei_category", nullable = false)
    private EventItemMap.Category memberEventCategory;
    
    @Column(name = "mei_gender")
    @Enumerated(EnumType.STRING) 
    private CoreUser.Gender memberEventGender;
    
    @Column(name = "mei_item_name")
    private String memberEventItemName;
    
    @Column(name = "mei_score")
    private Integer memberEventScore;
    
    @Column(name = "mei_grade")
    @Enumerated(EnumType.STRING)     // store the enum name in DB
    private Grade memberEventGrade;

    
    
    public enum Grade {
        GOLD,
        SILVER,
        BRONZE,
        PARTICIPATION
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mei_entry_by", nullable = true)
    private CoreUser scoreEntryBy;

    @Column(name = "mei_approve_datetime", nullable = true)
    private LocalDateTime approveDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mei_approve_by", nullable = true)
    private CoreUser approvedBy;
    
    
    @Column(name = "mei_certificate_status")
    @Enumerated(EnumType.STRING)
    private CertificateStatus certificateStatus;

    public enum CertificateStatus {
        PENDING,
        GENERATED,
        FAILED, 
        ACTIVE, 
        REVOKED
    }
    
    @Column(name = "mei_certificate_path")
    private String meiCertificatePath;
    
    @Column(name = "mei_certificate_file")
    private String meiCertificateFile;
    
    
    @Column(name = "mei_certificate_history", columnDefinition = "TEXT")
    private String certificateHistoryJson;

    // Use a single, static ObjectMapper with JavaTimeModule
    @Transient
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule())
            .disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    // Deserialize JSON to List<CertificateFileRecord>
    @Transient
    public List<CertificateFileRecord> getCertificateHistory() {
        try {
            if (certificateHistoryJson == null || certificateHistoryJson.isBlank()) {
                return new ArrayList<>();
            }
            return mapper.readValue(
                    certificateHistoryJson,
                    new TypeReference<List<CertificateFileRecord>>() {}
            );
        } catch (Exception e) {
            // Log the error if needed
            return new ArrayList<>();
        }
    }

    // Serialize List<CertificateFileRecord> to JSON
    public void setCertificateHistoryJson(String json) {
        this.certificateHistoryJson = json;
    }

    // Add a new certificate record
    public void addCertificateRecord(CertificateFileRecord record) {
        List<CertificateFileRecord> history = getCertificateHistory();
        history.add(record);
        try {
            this.certificateHistoryJson = mapper.writeValueAsString(history);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update certificate history JSON", e);
        }
    }



}
