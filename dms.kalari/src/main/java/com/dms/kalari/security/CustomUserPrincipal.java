package com.dms.kalari.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.dms.kalari.admin.dto.CoreUserDTO;
import com.dms.kalari.nodes.dto.NodeDTO;
import com.dms.kalari.nodes.entity.Node.Type;
import com.dms.kalari.admin.entity.CoreUser.UserType;

import java.util.*;
import java.util.Optional;

public class CustomUserPrincipal implements UserDetails {

    private final CoreUserDTO user;
    private final NodeDTO node;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Map<String, String> aliasToRealPath; // Store alias → real path mapping
    private final Map<String, String> aliasMappings; // Precomputed alias mappings for faster access

    public CustomUserPrincipal(
            CoreUserDTO user,
            NodeDTO node,
            Collection<? extends GrantedAuthority> authorities,
            Map<String, String> aliasToRealPath) {
        this.user = user;
        this.node = node;
        this.authorities = authorities;
        this.aliasToRealPath = aliasToRealPath;
        this.aliasMappings = Collections.unmodifiableMap(new HashMap<>(aliasToRealPath)); // Precompute for fast access
    }

    // Check if user has a specific privilege alias
    public boolean hasPrivilege(String alias) {
        return aliasMappings.containsKey(alias);
    }

    // Get real path for alias
    public Optional<String> getRealPath(String alias) {
        return Optional.ofNullable(aliasMappings.get(alias));
    }

    // Get all privileges
    public Set<String> getPrivileges() {
        return aliasMappings.keySet();
    }

    // Get the precomputed alias mappings (for optimized access)
    public Map<String, String> getAliasMappings() {
        return aliasMappings;
    }

    // Getters
    public Long getUserId() { return user.getUserId(); }
    public Long getRoleId() { return user.getUserDesig(); }
    public Long getInstId() { return user.getUserNode(); }
    public Type getNodeType() { return node.getNodeType(); }
    public UserType getUserType() { return user.getUserType(); }
    public String getUserFullName() { return user.getUserFname() + " " + user.getUserLname(); }
    public String getNodeName() { return node.getNodeName(); }

    // Spring Security methods
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override public String getPassword() { return user.getUserPassword(); }
    @Override public String getUsername() { return user.getUserEmail(); }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return user.getUserStatus() == 1; }
}