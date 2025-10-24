package com.dms.kalari.branch.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.dms.kalari.common.BaseEntity;

@Entity
@Table(name = "nodes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Node extends BaseEntity {

    public enum Type {
        ROOT(0), NATIONAL(1), STATE(2), DISTRICT(3), KALARI(4);

        private final int level;
        Type(int level) { this.level = level; }
        public int getLevel() { return level; }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "node_id")
    private Long nodeId;

    @Column(name = "node_name", nullable = false)
    @NotBlank(message = "Node name is mandatory")
    private String nodeName;

    @Enumerated(EnumType.STRING)
    @Column(name = "node_type", nullable = false)
    @NotNull(message = "Node type is mandatory")
    private Type nodeType;

    @Column(name = "node_status", nullable = false)
    @NotNull(message = "Node status is required")
    private Short nodeStatus = 1;

    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "node_id")
    private Node parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Node> children;

    @Column(name = "user_address_line1", nullable = true)
    @NotBlank(message = "Address Line 1 is mandatory")
    private String addressLine1;

    @Column(name = "user_address_line2", nullable = true)
    @NotBlank(message = "Address Line 2 is mandatory")
    private String addressLine2;

    @Column(name = "user_address_line3", nullable = true)
    @NotBlank(message = "Address Line 3 is mandatory")
    private String addressLine3;

    @Column(name = "user_address_state", nullable = true)
    @NotBlank(message = "State is mandatory")
    private String addressState;

    @Column(name = "user_address_pin", length = 6, nullable = true)
    @NotBlank(message = "PIN code is mandatory")
    @Size(min = 6, max = 6, message = "PIN must be exactly 6 digits")
    private String addressPin;

    @Column(name = "register_number", length = 20, nullable = true)
    @NotBlank(message = "Register number is mandatory")
    @Size(max = 20, message = "Register number cannot exceed 20 characters")
    private String registerNumber;
    
    @Column(name = "photo_file")
    private Long photoFile;
    
    @Column(name = "branch_history", nullable = true, length = 500)
    private String branchHistory;
    
    @Column(name = "branch_activity", nullable = true, length = 500)
    private String branchActivity;
    
    @Column(name = "branch_vision", nullable = true, length = 500)
    private String branchVision;
}
