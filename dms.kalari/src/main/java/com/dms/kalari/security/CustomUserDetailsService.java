package com.dms.kalari.security;

import com.dms.kalari.admin.dto.CoreUserDTO;
import com.dms.kalari.admin.dto.UserPrivilegeProjection;
import com.dms.kalari.admin.service.CoreUserService;
import com.dms.kalari.nodes.dto.NodeDTO;
import com.dms.kalari.nodes.service.NodeService;
import com.dms.kalari.admin.repository.AuthUserPrivilegeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired private CoreUserService coreUserService;
    @Autowired private AuthUserPrivilegeRepository privilegeRepo;
    @Autowired private NodeService nodeService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Load user
        CoreUserDTO user = coreUserService.getUserByEmailAddress(username);
        if (user == null) throw new UsernameNotFoundException("User not found: " + username);

        // 2. Check status
        if (user.getUserStatus() != 1) {
            throw new DisabledException("User account is inactive");
        }

        // 3. Load node information
        NodeDTO node = nodeService.findById(user.getUserNode());

        // 4. Load privileges with real path mapping
        Map<String, String> aliasToRealPath = loadPrivilegesWithRealPath(user.getUserDesig());
        List<GrantedAuthority> authorities = buildAuthorities(aliasToRealPath.keySet());

        // 5. Build principal
        return new CustomUserPrincipal(user, node, authorities, aliasToRealPath);
    }

    private Map<String, String> loadPrivilegesWithRealPath(Long roleId) {
        return privilegeRepo.findPrivilegesByRole(roleId).stream()
                .collect(Collectors.toMap(
                    UserPrivilegeProjection::getAlias,
                    UserPrivilegeProjection::getRealPath,
                    (existing, replacement) -> existing // Handle duplicates if any
                ));
    }

    private List<GrantedAuthority> buildAuthorities(Set<String> privilegeAliases) {
        return privilegeAliases.stream()
                .map(alias -> new SimpleGrantedAuthority("PRIV_" + alias))
                .collect(Collectors.toList());
    }
}