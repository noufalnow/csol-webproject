package com.dms.kalari.branch.entity;

import jakarta.persistence.*;
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
	    ROOT(0), COUNTRY(1), STATE(2), DISTRICT(3), KALARI(4);

	    private final int level;
	    Type(int level) { this.level = level; }
	    public int getLevel() { return level; }
	}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "node_id")
    private Long nodeId;

    @Column(name = "node_name", nullable = false)
    private String nodeName;

    @Enumerated(EnumType.STRING)
    @Column(name = "node_type", nullable = false)
    private Type nodeType;

    @Column(name = "node_status", nullable = false)
    private Short nodeStatus = 1;

    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "node_id")
    private Node parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Node> children;

}