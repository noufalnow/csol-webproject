package com.dms.kalari.nodes.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dms.kalari.common.BaseRepository;
import com.dms.kalari.nodes.entity.Node;

import java.util.List;
import java.util.Optional;

@Repository
public interface NodeRepository extends BaseRepository<Node, Long> {
    @Query("SELECT n FROM Node n WHERE n.nodeId = :nodeId AND n.deleted = false")
    Optional<Node> findByIdAndNotDeleted(Long nodeId);

    @Query("SELECT n FROM Node n WHERE n.deleted = false AND " +
    	       "(LOWER(n.nodeName) LIKE LOWER(CONCAT('%', :search, '%')))")
    	Page<Node> findAllNotDeleted(String search, Pageable pageable);

    @Query("SELECT n FROM Node n WHERE n.parent.nodeId = :parentId AND n.deleted = false")
    List<Node> findByParentId(Long parentId);

    @Query("SELECT n FROM Node n WHERE n.parent IS NULL AND n.deleted = false")
    List<Node> findRootNodes();

    boolean existsByNodeTypeAndDeletedFalse(Node.Type nodeType);


    @Query("SELECT COUNT(n) FROM Node n WHERE n.parent.nodeId = :parentId AND n.deleted = false")
    long countByParentId(Long parentId);
    
    
    @Query("SELECT COUNT(n) FROM Node n WHERE n.parent IS NULL AND n.deleted = false")
    long countRootNodes();
    
    
    @Query("""
    	    SELECT n 
    	    FROM Node n 
    	    LEFT JOIN FETCH n.children 
    	    WHERE n.nodeId = :nodeId AND n.deleted = false
    	    """)
    	Optional<Node> findByIdWithChildren(@Param("nodeId") Long nodeId);

}