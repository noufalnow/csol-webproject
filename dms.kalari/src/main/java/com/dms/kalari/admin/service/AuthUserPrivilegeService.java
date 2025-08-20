package com.dms.kalari.admin.service;

import com.dms.kalari.admin.dto.AppPageOperationDTO;
import com.dms.kalari.admin.repository.AuthAppPageOperationRepository;
import com.dms.kalari.admin.repository.AuthUserPrivilegeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthUserPrivilegeService {

    private final AuthAppPageOperationRepository opRepo;
    private final AuthUserPrivilegeRepository privRepo;

    public AuthUserPrivilegeService(AuthAppPageOperationRepository opRepo, AuthUserPrivilegeRepository privRepo) {
        this.opRepo = opRepo;
        this.privRepo = privRepo;
    }

    /**
     * Check whether (roleId, instId) can invoke alias+method.
     */
    public boolean hasAccess(Long roleId, Long instId, String alias, String method) {
        Optional<AppPageOperationDTO> op = opRepo.findActiveByAliasAndMethod(alias, method);
        if (op.isEmpty()) return false;

        AppPageOperationDTO o = op.get();
        long cnt = privRepo.countActivePrivilege(roleId, instId, o.getAppPageId(), o.getOperationId());
        return cnt > 0;
    }

    public Optional<AppPageOperationDTO> resolveAlias(String alias, String method) {
        return opRepo.findActiveByAliasAndMethod(alias, method);
    }
}
