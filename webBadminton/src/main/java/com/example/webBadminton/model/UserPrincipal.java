package com.example.webBadminton.model;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class UserPrincipal implements OAuth2User, UserDetails {
    private User user;
    private Map<String, Object> attributes;

    // Constructor
    public UserPrincipal(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    // OAuth2User methods
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return user.getUsername(); // Assuming User class has getUsername()
    }

    // UserDetails methods
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (user.getRole() != null) {
            return Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getName()));
        } else {
            return Collections.emptyList();  // Return an empty list if no roles
        }
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // Assuming User class has getPassword()
    }

    @Override
    public String getUsername() {
        return user.getUsername(); // Assuming User class has getUsername()
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Implement based on your needs
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Implement based on your needs
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Implement based on your needs
    }
}
