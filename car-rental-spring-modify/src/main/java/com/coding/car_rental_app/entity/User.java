package com.coding.car_rental_app.entity;

import com.coding.car_rental_app.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

//@JsonIgnoreProperties why here?
//spring security uses UserDetails object internally
//and our custom entity 'User' implements UserDetails
//so when spring tries to deserialize the cached UserDetails from redis
//it uses Jackson(JSON) deserialization
//However the entity 'User' not contain a field called 'enabled' directly
//BUT spring security's UserDetails interface has a method like isEnabled(), isAccountExpired()
//Redis (via Jackson) serialize the object, and this properties (enabled, etc) get stored.
//NOW on deserialization Jackson sees 'enabled' property but can not map it to a field leading to ERROR.
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "users")
@Data
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String name;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;


    @JsonIgnore //to exclude authorities from redis serialization and prevent deserialization issues
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userRole.name()));
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

}
