package com.dms.kalari.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.dms.kalari.admin.entity.CoreUser;
import com.dms.kalari.admin.entity.CoreUser.UserType;
import com.dms.kalari.entity.Node;
import com.dms.kalari.entity.Node.Type;

import java.util.*;

public class CustomUserPrincipal implements UserDetails {

    private final Long userId;
    private final String username; // email
    private final String password; // hashed
    private final Long roleId;
    private final Long instId;     // nullable
    private final Type nodeType;
    private final UserType userType;
    private final String userFullName;
    private final String nodeName;

    private final Collection<? extends GrantedAuthority> authorities;

    // Privilege cache
    private final Map<String, String> aliasToPath;
    private final Map<String, Long> aliasToOperationId;
    private final Map<String, Long> aliasToAppPageId;

    public CustomUserPrincipal(
            Long userId,
            String username,
            String password,
            Long roleId,
            Long instId,
            Node.Type nodeType,           // Change to enum type
            CoreUser.UserType userType,   // Change to enum type
            String userFullName,
            String nodeName,
            Collection<? extends GrantedAuthority> authorities,
            Map<String, String> aliasToPath,
            Map<String, Long> aliasToOperationId,
            Map<String, Long> aliasToAppPageId
    ) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.roleId = roleId;
        this.instId = instId;
        this.nodeType = nodeType;
        this.userType = userType;
        this.userFullName = userFullName;
        this.nodeName = nodeName;
        this.authorities = authorities;
        this.aliasToPath = aliasToPath;
        this.aliasToOperationId = aliasToOperationId;
        this.aliasToAppPageId = aliasToAppPageId;
    }

    // ✅ Session equivalents
    public Long getUserId() { return userId; }
    public Long getRoleId() { return roleId; }
    public Long getInstId() { return instId; }
    public Type getNodeType() { return nodeType; }
    public UserType getUserType() { return userType; }
    public String getUserFullName() { return userFullName; }
    public String getNodeName() { return nodeName; }

    // ✅ Privileges
    public boolean hasAlias(String alias) { return aliasToPath.containsKey(alias); }
    public Optional<String> resolveRealPath(String alias) { return Optional.ofNullable(aliasToPath.get(alias)); }
    public Optional<Long> getOperationId(String alias) { return Optional.ofNullable(aliasToOperationId.get(alias)); }
    public Optional<Long> getAppPageId(String alias) { return Optional.ofNullable(aliasToAppPageId.get(alias)); }

    // Spring Security boilerplate
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override public String getPassword() { return password; }
    @Override public String getUsername() { return username; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
