package com.example.tenant_service.service;

import com.example.tenant_service.common.BaseService;
import com.example.tenant_service.dto.MisCollectionDTO;
import com.example.tenant_service.entity.MisCollection;
import com.example.tenant_service.exception.ResourceNotFoundException;
import com.example.tenant_service.mapper.MisCollectionMapper;
import com.example.tenant_service.repository.MisCollectionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MisCollectionService implements BaseService<MisCollectionDTO> {

    private final MisCollectionRepository misCollectionRepository;
    private final MisCollectionMapper misCollectionMapper;
    private final EntityManager entityManager;

    @Autowired
    public MisCollectionService(MisCollectionRepository misCollectionRepository, MisCollectionMapper misCollectionMapper,
                                EntityManager entityManager) {
        this.misCollectionRepository = misCollectionRepository;
        this.misCollectionMapper = misCollectionMapper;
        this.entityManager = entityManager;
    }

    @Override
    public MisCollectionDTO update(Long collId, MisCollectionDTO misCollectionDTO) {
        MisCollection existingCollection = misCollectionRepository.findById(collId)
                .orElseThrow(() -> new ResourceNotFoundException("MisCollection", collId));
        MisCollection updatedCollection = misCollectionRepository.save(existingCollection);
        return misCollectionMapper.toDTO(updatedCollection);
    }

    @Override
    public List<MisCollectionDTO> findAll() {
        return misCollectionRepository.findAll().stream()
                .map(misCollectionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MisCollectionDTO findById(Long collId) {
        return misCollectionRepository.findById(collId)
                .map(misCollectionMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("MisCollection", collId));
    }

    @Override
    public MisCollectionDTO save(MisCollectionDTO misCollectionDTO) {
        MisCollection misCollection = misCollectionMapper.toEntity(misCollectionDTO);
        MisCollection savedCollection = misCollectionRepository.save(misCollection);
        return misCollectionMapper.toDTO(savedCollection);
    }

    @Override
    public void softDeleteById(Long collId) {
        MisCollection collection = misCollectionRepository.findById(collId)
                .orElseThrow(() -> new ResourceNotFoundException("MisCollection", collId));
        collection.setDeleted(true);
        misCollectionRepository.save(collection);
    }

    @Override
    public Page<MisCollectionDTO> findAllPaginate(Pageable pageable, String search) {
        return misCollectionRepository.findAllNotDeleted(search == null ? "" : search, pageable)
                .map(misCollectionMapper::toDTO);
    }

    public List<MisCollectionDTO> findAllCollectionReport(Long customer, String property, LocalDate fromDate, LocalDate toDate) {
        List<MisCollection> collections = misCollectionRepository.findAllByFilters(customer, property, fromDate, toDate);
        return collections.stream()
                .map(misCollectionMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<Object[]> getCustomerPayments(String customer, LocalDate fromDate, LocalDate toDate, String sortField, String sortDir) {// Building a dynamic SQL query based on input parameters
    	StringBuilder queryBuilder = new StringBuilder();
    	queryBuilder.append("""
    	    SELECT t.tnt_full_name,
    	           t.tnt_comp_name,
    	           t.tnt_phone,
    	           tenant_due.due_amount,
    	           tenant_due.total_amount,
    	           tenant_coll.paid_amount
    	    FROM mis_tenants t
    	    LEFT JOIN
    	      (SELECT tenents.tnt_id,
    	              SUM(CASE WHEN popt_status = 1 THEN popt_amount ELSE 0 END) AS due_amount,
    	              SUM(popt_amount) AS total_amount
    	       FROM mis_property_payoption payop
    	       LEFT JOIN mis_documents agdocs ON agdocs.doc_id = payop.popt_doc_id
    	       AND agdocs.deleted = 'f'
    	       LEFT JOIN mis_tenants tenents ON agdocs.doc_tnt_id = tenents.tnt_id
    	       AND tenents.deleted = 'f'
    	       WHERE payop.deleted = 'f'
    	       GROUP BY tenents.tnt_id) tenant_due ON tenant_due.tnt_id = t.tnt_id
    	    LEFT JOIN
    	      (SELECT SUM(cdet_amt_paid) AS paid_amount,
    	              coll_cust
    	       FROM mis_collection colln
    	       LEFT JOIN mis_collection_det colldet ON colldet.cdet_coll_id = colln.coll_id
    	       WHERE colln.deleted = 'f'
    	       GROUP BY coll_cust) tenant_coll ON tenant_coll.coll_cust = t.tnt_id
    	""");

    	// Adding WHERE clause if filters are provided
    	String whereClause = "";
    	if (customer != null && !customer.isEmpty()) {
    	    whereClause += " t.tnt_full_name ILIKE :customer";
    	}
    	if (fromDate != null) {
    	    if (!whereClause.isEmpty()) {
    	        whereClause += " AND ";
    	    }
    	    whereClause += " tenant_due.due_amount IS NOT NULL"; // Adjusted since `due_date` doesn't exist
    	    whereClause += " AND tenant_due.due_date >= :fromDate"; // Optional if you want to filter by other date fields
    	}
    	if (toDate != null) {
    	    if (!whereClause.isEmpty()) {
    	        whereClause += " AND ";
    	    }
    	    whereClause += " tenant_due.due_date <= :toDate";
    	}

    	// Append the WHERE clause if any filters are applied
    	if (!whereClause.isEmpty()) {
    	    queryBuilder.append(" WHERE ").append(whereClause);
    	}

    	// Adding sorting logic
    	queryBuilder.append("""
    	    ORDER BY CASE WHEN :sortDir = 'asc' THEN
    	                     CASE :sortField
    	                         WHEN 'tnt_full_name' THEN t.tnt_full_name
    	                         WHEN 'tnt_comp_name' THEN t.tnt_comp_name
    	                         WHEN 'tnt_phone' THEN t.tnt_phone
    	                         ELSE t.tnt_full_name END
    	                 ELSE
    	                     CASE :sortField
    	                         WHEN 'tnt_full_name' THEN t.tnt_full_name
    	                         WHEN 'tnt_comp_name' THEN t.tnt_comp_name
    	                         WHEN 'tnt_phone' THEN t.tnt_phone
    	                         ELSE t.tnt_full_name END
    	             END
    	""");

    	// Creating the query
    	Query query = entityManager.createNativeQuery(queryBuilder.toString());

    	// Setting query parameters
    	if (customer != null && !customer.isEmpty()) {
    	    query.setParameter("customer", "%" + customer + "%");
    	}
    	if (fromDate != null) {
    	    query.setParameter("fromDate", fromDate);
    	}
    	if (toDate != null) {
    	    query.setParameter("toDate", toDate);
    	}
    	query.setParameter("sortField", sortField);
    	query.setParameter("sortDir", sortDir);

    	// Executing the query and returning the result
    	return query.getResultList();
}
}
