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
        // Use more descriptive variable names
        List<AuthAppPageOperation> availableOperations = privRepo.findOperationsByModuleId(moduleId);
        
        // Consider using a dedicated DTO projection from repository
        Set<String> existingPermissionKeys = privRepo.findRolePermissionKeys(roleId, moduleId);
        
        Map<Long, PageWithOperations> pageMap = new LinkedHashMap<>();

        for (AuthAppPageOperation operation : availableOperations) {
            if (operation.getAppPage() == null) continue;
            
            AuthAppPage page = operation.getAppPage();
            PageWithOperations pageDto = pageMap.computeIfAbsent(page.getAppPageId(), id -> 
                new PageWithOperations(page.getAppPageId(), page.getPageName())
            );

            String permissionKey = page.getAppPageId() + "_" + operation.getOperationId();
            boolean hasPermission = existingPermissionKeys.contains(permissionKey);
            
            pageDto.addOperation(new OperationWithPermission(
                operation.getOperationId(),
                operation.getOperationName(),
                operation.getAlias(),
                hasPermission
            ));
        }

        // Calculate hasAllPermissions for each page
        pageMap.values().forEach(PageWithOperations::calculateHasAllPermissions);
        
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
    public void updatePrivileges(Long roleId, Long moduleId, List<Object[]> pageOperationPairs) {

        // delete existing privileges for this role and module
        privRepo.deleteByRoleIdAndModuleIdWithExclusions(roleId, moduleId, 1L, 1L);

        if (pageOperationPairs != null && !pageOperationPairs.isEmpty()) {
            List<AuthUserPrivilege> newPrivileges = pageOperationPairs.stream()
                    .map(pair -> {
                        Long pageId = (Long) pair[0];
                        Long operationId = (Long) pair[1];
                        
                        AuthUserPrivilege privilege = new AuthUserPrivilege();
                        privilege.setRoleId(roleId);
                        privilege.setModuleId(moduleId);
                        AuthAppPageOperation operation = entityManager.getReference(AuthAppPageOperation.class, operationId);
                        privilege.setOperation(operation);

                        // Get a reference to the app page using the extracted pageId
                        AuthAppPage appPage = entityManager.getReference(AuthAppPage.class, pageId);
                        privilege.setAppPage(appPage);

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
