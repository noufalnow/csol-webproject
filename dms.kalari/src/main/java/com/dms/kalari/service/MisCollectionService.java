package com.dms.kalari.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;

import org.hibernate.query.NativeQuery;
import org.hibernate.sql.ast.tree.expression.SqlTuple;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dms.kalari.common.BaseService;
import com.dms.kalari.dto.MisCollectionDTO;
import com.dms.kalari.entity.MisCollection;
import com.dms.kalari.exception.ResourceNotFoundException;
import com.dms.kalari.mapper.MisCollectionMapper;
import com.dms.kalari.repository.MisCollectionRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MisCollectionService implements BaseService<MisCollectionDTO> {

	private final MisCollectionRepository misCollectionRepository;
	private final MisCollectionMapper misCollectionMapper;
	private final EntityManager entityManager;

	@Autowired
	public MisCollectionService(MisCollectionRepository misCollectionRepository,
			MisCollectionMapper misCollectionMapper, EntityManager entityManager) {
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
		return misCollectionRepository.findAll().stream().map(misCollectionMapper::toDTO).collect(Collectors.toList());
	}

	@Override
	public MisCollectionDTO findById(Long collId) {
		return misCollectionRepository.findById(collId).map(misCollectionMapper::toDTO)
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

	public List<MisCollectionDTO> findAllCollectionReport(Long customer, String property, LocalDate fromDate,
			LocalDate toDate) {
		List<MisCollection> collections = misCollectionRepository.findAllByFilters(customer, property, fromDate,
				toDate);
		return collections.stream().map(misCollectionMapper::toDTO).collect(Collectors.toList());
	}

	public List<Object[]> getCustomerPayments(String customer, LocalDate fromDate, LocalDate toDate, String sortField,
			String sortDir) {// Building a dynamic SQL query based on input parameters
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
		List<String> whereClauses = new ArrayList<>();

		// Add condition for customer name if provided
		if (customer != null && !customer.isEmpty()) {
			whereClauses.add("t.tnt_full_name ILIKE :customer");
		}

		// Add condition for due amount (as per your adjustment) and fromDate if
		// provided
		if (fromDate != null) {
			whereClauses.add("tenant_due.due_amount IS NOT NULL");
			whereClauses.add("tenant_due.due_date >= :fromDate");
		}

		// Add condition for toDate if provided
		if (toDate != null) {
			whereClauses.add("tenant_due.due_date <= :toDate");
		}

		// Append the dynamically built WHERE clause if there are any conditions
		if (!whereClauses.isEmpty()) {
			queryBuilder.append(" WHERE ");
			queryBuilder.append(String.join(" AND ", whereClauses));
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
	
	
	
	
	public List<Tuple> propertyListReport(String sortField, String sortDir, String propNo, String propName, String propFileno, String propBuilding, String status) {
	    StringBuilder queryBuilder = new StringBuilder("""
	            SELECT prop_id AS propId, prop_no AS propNo, prop_name AS propName,
	                   prop_fileno AS propFileno, prop_building AS propBuilding,
	                   prop_responsible AS propResponsible, prop_remarks AS propRemarks,
	                   u_created AS uCreated, u_modified AS uModified, 
	                   active AS active, prop_account AS propAccount
	            FROM mis_property
	            WHERE deleted='f'
	        """);

	    // Add filters dynamically if they are provided
	    if (propNo != null && !propNo.isEmpty()) {
	        queryBuilder.append(" AND prop_no LIKE :propNo");
	    }
	    if (propName != null && !propName.isEmpty()) {
	        queryBuilder.append(" AND prop_name LIKE :propName");
	    }
	    if (propFileno != null && !propFileno.isEmpty()) {
	        queryBuilder.append(" AND prop_fileno LIKE :propFileno");
	    }
	    if (propBuilding != null && !propBuilding.isEmpty()) {
	        queryBuilder.append(" AND prop_building LIKE :propBuilding");
	    }
	    if (status != null && !status.isEmpty()) {
	        queryBuilder.append(" AND active = :status");
	    }

	    // Apply sorting
	    queryBuilder.append(" ORDER BY ").append(sortField).append(" ").append(sortDir);

	    // Create and execute query
	    Query query = entityManager.createNativeQuery(queryBuilder.toString(), Tuple.class);
	    
	    // Set filter parameters
	    if (propNo != null && !propNo.isEmpty()) {
	        query.setParameter("propNo", "%" + propNo + "%");
	    }
	    if (propName != null && !propName.isEmpty()) {
	        query.setParameter("propName", "%" + propName + "%");
	    }
	    if (propFileno != null && !propFileno.isEmpty()) {
	        query.setParameter("propFileno", "%" + propFileno + "%");
	    }
	    if (propBuilding != null && !propBuilding.isEmpty()) {
	        query.setParameter("propBuilding", "%" + propBuilding + "%");
	    }
	    if (status != null && !status.isEmpty()) {
	        query.setParameter("status", "active".equals(status) ? 1 : 0);  // Assuming 1=active and 0=inactive
	    }

	    return query.getResultList();
	}

	
	
	/*public List<Tuple> propertyListReport(String sortField, String sortDir) {
	    StringBuilder queryBuilder = new StringBuilder("""
	            SELECT prop_id AS propId, prop_no AS propNo, prop_name AS propName,
	                   prop_fileno AS propFileno, prop_building AS propBuilding,
	                   prop_responsible AS propResponsible, prop_remarks AS propRemarks,
	                   u_created AS uCreated, u_modified AS uModified, 
	                   active AS active, prop_account AS propAccount
	            FROM mis_property
	        """);

	    // Apply sorting if sortField and sortDir are provided
	    if (sortField != null && sortDir != null) {
	        queryBuilder.append(" ORDER BY ")
	                    .append(sortField)
	                    .append(" ")
	                    .append(sortDir);
	    }

	    // Execute the query
	    Query query = entityManager.createNativeQuery(queryBuilder.toString(), Tuple.class);
	    return query.getResultList();
	}*/




}
