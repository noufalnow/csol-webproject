package com.dms.kalari.admin.service;

import com.dms.kalari.admin.dto.AuthUserPrivilegeDTO;
import com.dms.kalari.admin.dto.OperationWithPermission;
import com.dms.kalari.admin.dto.PageWithOperations;
import com.dms.kalari.admin.entity.AuthAppPage;
import com.dms.kalari.admin.entity.AuthAppPageOperation;
import com.dms.kalari.admin.entity.AuthUserPrivilege;
import com.dms.kalari.admin.mapper.AuthUserPrivilegeMapper;
import com.dms.kalari.admin.repository.AuthAppPageOperationRepository;
import com.dms.kalari.admin.repository.AuthUserPrivilegeRepository;
import com.dms.kalari.common.BaseMapper;
import com.dms.kalari.common.BaseService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

	public class AuthUserPrivilegeService implements BaseService<AuthUserPrivilegeDTO> {

    private final AuthAppPageOperationRepository opRepo;
    private final AuthUserPrivilegeRepository privRepo;
    
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Build module permissions (Page → Operations) for a role.
     */
    public List<PageWithOperations> getModulePermissions(Long roleId, Long moduleId) {
        // Get ALL available operations for the module
        List<AuthAppPageOperation> allOperations = privRepo.findOperationsByModuleId(moduleId);
        
        // Get existing role privileges
        Set<String> existingPermissions = privRepo.findByRoleIdAndModuleId(roleId, moduleId)
            .stream()
            .filter(p -> p.getAppPage() != null && p.getOperation() != null)
            .map(p -> p.getAppPage().getAppPageId() + "_" + p.getOperation().getOperationId())
            .collect(Collectors.toSet());

        // DEBUG
        System.out.println("Existing permissions: " + existingPermissions);

        Map<Long, PageWithOperations> pageMap = new LinkedHashMap<>();

        for (AuthAppPageOperation operation : allOperations) {
            if (operation.getAppPage() == null) {
                continue;
            }
            
            Long pageId = operation.getAppPage().getAppPageId();
            String pageName = operation.getAppPage().getPageName();

            PageWithOperations page = pageMap.computeIfAbsent(pageId, id -> {
                PageWithOperations newPage = new PageWithOperations();
                newPage.setPageId(pageId);
                newPage.setPageName(pageName);
                newPage.setOperations(new ArrayList<>());
                return newPage;
            });

            // Add operation - use the direct fields from AuthAppPageOperation
            OperationWithPermission operationDto = new OperationWithPermission();
            operationDto.setOperationId(operation.getOperationId()); // Use the ID from AuthAppPageOperation
            operationDto.setOperationName(operation.getOperation()); // Use the 'operation' field
            operationDto.setAlias(operation.getAlias()); // Use the 'alias' field
            
            String permissionKey = pageId + "_" + operation.getOperationId();
            
            boolean hasPermission = existingPermissions.contains(permissionKey);
            operationDto.setHasPermission(hasPermission);
            
            System.out.println("Checking permission for: " + permissionKey + " = " + hasPermission);

            page.getOperations().add(operationDto);
        }

        // Calculate hasAllPermissions for each page
        for (PageWithOperations page : pageMap.values()) {
            boolean hasAll = !page.getOperations().isEmpty() && 
                            page.getOperations().stream()
                                .allMatch(OperationWithPermission::isHasPermission);
            page.setHasAllPermissions(hasAll);
        }

        return new ArrayList<>(pageMap.values());
    }

    /**
     * Page-level permission status (does role have ALL ops for each page?).
     */
    public Map<Long, Boolean> getPagePermissionStatus(Long roleId, Long moduleId) {
        List<Object[]> results = privRepo.countPagePermissions(roleId, moduleId);
        Map<Long, Boolean> pageStatus = new HashMap<>();

        for (Object[] result : results) {
            Long pageId = ((Number) result[0]).longValue();
            Boolean hasAllPermissions = (Boolean) result[1];
            pageStatus.put(pageId, hasAllPermissions);
        }

        return pageStatus;
    }

    public List<AuthUserPrivilege> getPrivilegesForRoleAndModule(Long roleId, Long moduleId) {
        return privRepo.findByRoleIdAndModuleId(roleId, moduleId);
    }

    public List<?> getModuleResources(Long moduleId) {
        return privRepo.findModuleResources(moduleId);
    }

    /**
     * Update privileges for a role and module.
     * NOTE: Fill in the actual appPage + operation objects properly before save.
     * @param authUserPrivilegeDTO 
     */
    @Transactional
    public void updatePrivileges(Long roleId, Long moduleId, Collection<Long> operationIds) {

        // delete existing privileges, except exclusions
        privRepo.deleteByRoleIdAndModuleIdWithExclusions(roleId, moduleId, 1L, 1L);

        if (operationIds != null && !operationIds.isEmpty()) {
            List<AuthUserPrivilege> newPrivileges = operationIds.stream()
                    .map(operationId -> {
                        AuthUserPrivilege privilege = new AuthUserPrivilege();
                        privilege.setRoleId(roleId);
                        privilege.setModuleId(moduleId);

                        // Get a reference to the operation
                        AuthAppPageOperation operation = entityManager.getReference(AuthAppPageOperation.class, operationId);
                        privilege.setOperation(operation);

                        // Derive the page from the operation
                        privilege.setAppPage(operation.getAppPage());

                        privilege.setActive((short) 1);
                        privilege.setInstId(1L);
                        return privilege;
                    })
                    .collect(Collectors.toList());

            privRepo.saveAll(newPrivileges);
        }
    }


	@Override
	public List<AuthUserPrivilegeDTO> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AuthUserPrivilegeDTO findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AuthUserPrivilegeDTO save(AuthUserPrivilegeDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AuthUserPrivilegeDTO update(Long id, AuthUserPrivilegeDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void softDeleteById(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Page<AuthUserPrivilegeDTO> findAllPaginate(Pageable pageable, String search) {
		// TODO Auto-generated method stub
		return null;
	}
}
