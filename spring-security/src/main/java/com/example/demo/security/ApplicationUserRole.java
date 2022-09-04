package com.example.demo.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum ApplicationUserRole {
    STUDENT(Sets.newHashSet()),
    ADMIN(Sets.newHashSet(
        ApplicationUserPermission.COURSE_READ,
        ApplicationUserPermission.COURSE_WRITE,
        ApplicationUserPermission.STUDENT_READ,
        ApplicationUserPermission.STUDENT_WRITE
    )),
    ADMINTRAINEE(Sets.newHashSet(
        ApplicationUserPermission.COURSE_READ,
        ApplicationUserPermission.STUDENT_READ
    ));

    private final Set<ApplicationUserPermission> permissions;


    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }


    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }


    public Set<GrantedAuthority> grantedAuthorities() {
        Set<GrantedAuthority> authorities = getPermissions().stream()
            .map(permissions -> new SimpleGrantedAuthority(
                permissions.getPermission()))
            .collect(Collectors.toSet());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
