package com.dms.kalari.branch.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dms.kalari.branch.dto.NodeFlatDTO;
import com.dms.kalari.branch.entity.Node;
import com.dms.kalari.common.BaseRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NodeRepository extends BaseRepository<Node, Long> {

	@Query("SELECT n FROM Node n WHERE n.nodeId = :nodeId AND n.deleted = false")
	Optional<Node> findByIdAndNotDeleted(@Param("nodeId") Long nodeId);

	@Query("SELECT n FROM Node n WHERE n.deleted = false AND "
			+ "(LOWER(n.nodeName) LIKE LOWER(CONCAT('%', :search, '%')))")
	Page<Node> findAllNotDeleted(@Param("search") String search, Pageable pageable);

	@Query("SELECT n FROM Node n WHERE n.parent.nodeId = :parentId AND n.deleted = false " + "ORDER BY n.nodeName ASC")
	List<Node> findByParentId(@Param("parentId") Long parentId);

	@Query("SELECT n FROM Node n WHERE n.parent IS NULL AND n.deleted = false")
	List<Node> findRootNodes();

	boolean existsByNodeTypeAndDeletedFalse(Node.Type nodeType);

	@Query("SELECT COUNT(n) FROM Node n WHERE n.parent.nodeId = :parentId AND n.deleted = false")
	long countByParentId(@Param("parentId") Long parentId);

	@Query("SELECT COUNT(n) FROM Node n WHERE n.parent IS NULL AND n.deleted = false")
	long countRootNodes();

	@Query("""
			    SELECT n
			    FROM Node n
			    LEFT JOIN FETCH n.children
			    WHERE n.nodeId = :nodeId AND n.deleted = false
			""")
	Optional<Node> findByIdWithChildren(@Param("nodeId") Long nodeId);

	/**
	 * Recursive query (PostgreSQL) to fetch full subtree for a given node
	 */
	@Query(value = """
			WITH RECURSIVE node_hierarchy AS (
			    SELECT n.node_id,
			           n.parent_id,
			           n.node_name,
			           n.node_type,
			           n.node_status,
			           0 AS lvl
			    FROM nodes n
			    WHERE n.node_id = :nodeId AND n.deleted = false

			    UNION ALL

			    SELECT c.node_id,
			           c.parent_id,
			           c.node_name,
			           c.node_type,
			           c.node_status,
			           nh.lvl + 1
			    FROM nodes c
			    JOIN node_hierarchy nh ON c.parent_id = nh.node_id
			    WHERE c.deleted = false
			)
			SELECT n.node_id   AS nodeId,
			       n.parent_id AS parentId,
			       n.node_name AS nodeName,
			       n.node_type AS nodeType,
			       n.node_status AS nodeStatus,
			       lvl
			FROM node_hierarchy n
			ORDER BY lvl, parent_id, node_name;

			    	    """, nativeQuery = true)
	List<NodeFlatDTO> findSubTreeFlat(@Param("nodeId") Long nodeId);

}
