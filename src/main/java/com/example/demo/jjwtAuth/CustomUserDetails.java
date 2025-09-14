package com.example.demo.jjwtAuth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.example.demo.Entities.UserEntity;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final UserEntity user;

    public CustomUserDetails(UserEntity user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Provide role as ROLE_STUDENT, ROLE_STAFF, etc.
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getId();  // Important: We use ID as the principal
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // Customize if needed
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // Customize if needed
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // Customize if needed
    }

    @Override
    public boolean isEnabled() {
        return user.isVerified();  // Only allow verified users
    }

    public String getRole() {
        return user.getRole();
    }

    public String getMail() {
        return user.getMail();
    }
}
