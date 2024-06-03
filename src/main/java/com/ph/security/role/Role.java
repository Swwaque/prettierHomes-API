package com.ph.security.role;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum Role {
    ADMIN(
            Set.of(
                    Permission.ADMIN,
                    Permission.MANAGER,
                    Permission.CUSTOMER
            )
    ),
    MANAGER(
            Set.of(
                    Permission.MANAGER,
                    Permission.CUSTOMER
            )
    ),
    CUSTOMER(
            Set.of(
                    Permission.CUSTOMER
            )
    );

    @Getter
    private final Set<Permission> permissions;

    /**
     * Returns the list of authorities for the current user.
     *
     * @return the list of authorities
     */
    public List<SimpleGrantedAuthority> getAuthorities() {

        // Get the list of permissions and map them to SimpleGrantedAuthority objects
        List<SimpleGrantedAuthority> authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());

        return authorities;
    }

}
