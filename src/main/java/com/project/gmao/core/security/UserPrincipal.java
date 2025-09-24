package com.project.gmao.core.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.project.gmao.features.authentication.entity.User;

import java.util.Collection;
import java.util.Set;
import java.util.HashSet;

@Data
@AllArgsConstructor
public class UserPrincipal implements UserDetails {

    private Long id;
    private String prenom;
    private String nom;
    private String email;
    private String telephone;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean enabled;

    public static UserPrincipal create(User user) {
        Set<GrantedAuthority> authorities = new HashSet<>();

        user.getRoles().forEach(role -> {
            // Add role authority
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));

            // Add permission authorities - fixed: removed extra .getName() call
            role.getPermissions().forEach(permission ->
                    authorities.add(new SimpleGrantedAuthority(permission.getName()))
            );
        });

        return new UserPrincipal(
                user.getId(),
                user.getPrenom(),
                user.getNom(),
                user.getEmail(),
                user.getTelephone(),
                user.getPassword(),
                authorities,
                user.isEnabled()
        );
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}