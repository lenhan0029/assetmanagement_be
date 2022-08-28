package com.nashtech.rookies.AssetManagement.security.service;

import java.util.Collection;
import java.util.Collections;
//import java.util.List;
import java.util.Objects;
//import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nashtech.rookies.AssetManagement.model.entities.Accounts;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private int id;

    private String email;

    @JsonIgnore
    private String password;

    private  boolean first_login;


    private Collection<? extends GrantedAuthority> authorities;


    public UserDetailsImpl(int id, String email, String password,boolean first_login,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;   
        this.email = email;
        this.password = password;
        this.first_login = first_login;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(Accounts account) {
//        List<GrantedAuthority> authorities = account.getRoles().stream()
//            .map(role -> new SimpleGrantedAuthority(role.getName().name()))
//            .collect(Collectors.toList());
    	
    	Collection<? extends GrantedAuthority> authorities = Collections
    			.singleton(new SimpleGrantedAuthority(account.getRole().getRoleName()));

        return new UserDetailsImpl(
        		account.getAccountId(),        	
        		account.getUserName(),
        		account.getPassword(),
                account.isFirtLogin(),
            authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
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
        return true;
    }

    public boolean isFirst_login() {
        return first_login;
    }

    public void setFirst_login(boolean first_login) {
        this.first_login = first_login;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}