package com.acamus.telegrm.core.domain.model;

import com.acamus.telegrm.core.domain.valueobjects.Email;
import com.acamus.telegrm.core.domain.valueobjects.Password;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class User {

    private String id;
    private Email email;
    private Password password;
    private String name;
    private boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;

    private User() {
    }

    // Factory method:
    public static User create(String name, Email email, Password password) {
        User user = new User();
        user.id = UUID.randomUUID().toString();
        user.name = name;
        user.email = Objects.requireNonNull(email);
        user.password = Objects.requireNonNull(password);
        user.enabled = true;
        user.createdAt = LocalDateTime.now();
        return user;
    }

    // Reconstrucción desde repositorio
    public static User reconstruct(String id, String name, Email email, Password password,
                                   boolean enabled, LocalDateTime createdAt, LocalDateTime lastLoginAt) {
        User user = new User();
        user.id = Objects.requireNonNull(id);
        user.name = name;
        user.email = Objects.requireNonNull(email);
        user.password = Objects.requireNonNull(password);
        user.enabled = enabled;
        user.createdAt = Objects.requireNonNull(createdAt);
        user.lastLoginAt = lastLoginAt;
        return user;
    }

    // Getters
    public String getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public Password getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

