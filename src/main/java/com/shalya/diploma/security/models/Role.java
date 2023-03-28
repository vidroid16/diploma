package com.shalya.diploma.security.models;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    USER(new HashSet<>(Arrays.asList(Permission.PROJECT_CREATE, Permission.PROJECT_FIND, Permission.KICK_STARTER_DONATE))),
    ADMIN(new HashSet<>(Arrays.asList(Permission.PROJECT_CREATE, Permission.PROJECT_FIND, Permission.KICK_STARTER_DONATE, Permission.USERS_CREATE, Permission.USERS_LIST)));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}
