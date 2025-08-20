package com.dms.kalari.security;

import com.dms.kalari.admin.dto.CoreUserDTO;
import com.dms.kalari.admin.dto.UserPrivilegeProjection;
import com.dms.kalari.admin.entity.CoreUser;
import com.dms.kalari.admin.repository.AuthUserPrivilegeRepository;
import com.dms.kalari.admin.service.CoreUserService;
import com.dms.kalari.dto.NodeDTO;
import com.dms.kalari.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import com.dms.kalari.entity.Node;
import com.dms.kalari.entity.Node.Type;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired private CoreUserService coreUserService;
    @Autowired private AuthUserPrivilegeRepository privilegeRepo;
    @Autowired private NodeService nodeService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Load user
        CoreUserDTO user = coreUserService.getUserByEmailAddress(username);
        if (user == null) throw new UsernameNotFoundException("User not found: " + username);

        // 2. Check status (like old service)
        if (user.getUserStatus() != 1) {
            throw new DisabledException("User account is inactive");
        }

        Long roleId = user.getUserDesig();
        Long instId = user.getUserNode();

        NodeDTO node = nodeService.findById(instId);

     // 3. Preload privileges
        List<UserPrivilegeProjection> rows = privilegeRepo.findPrivilegesByRole(roleId);

        Map<String, String> aliasToPath = new HashMap<>();
        Map<String, Long> aliasToOpId = new HashMap<>();
        for (UserPrivilegeProjection p : rows) {
            aliasToPath.put(p.getAlias(), p.getRealPath());
            aliasToOpId.put(p.getAlias(), p.getOperationId());
        }

        List<GrantedAuthority> authorities = rows.stream()
                .map(r -> new SimpleGrantedAuthority("PRIV_" + r.getAlias()))
                .collect(Collectors.toList());


        // 4. Build full principal (rich version)
        return new CustomUserPrincipal(
                user.getUserId(),                   // Long userId
                user.getUserEmail(),                // String username (email)
                user.getUserPassword(),             // String password (hashed)
                roleId,                             // Long roleId
                instId,                             // Long instId
                node.getNodeType(),                 // ✅ Already Node.Type enum
                user.getUserType(),                 // ✅ Already CoreUser.UserType enum
                user.getUserFname() + " " + user.getUserLname(), // String userFullName
                node.getNodeName(),                 // String nodeName
                authorities,                        // Collection<? extends GrantedAuthority>
                aliasToPath,                        // Map<String, String>
                aliasToOpId,                        // Map<String, Long>
                Collections.emptyMap()              // Map<String, Long> aliasToAppPageId (not built yet)
        );


    }
}
