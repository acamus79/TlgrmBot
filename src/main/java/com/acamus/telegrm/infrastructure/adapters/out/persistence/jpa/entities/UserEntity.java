package com.acamus.telegrm.infrastructure.adapters.out.persistence.jpa.entities;

import com.acamus.telegrm.core.domain.model.User;
import com.acamus.telegrm.core.domain.valueobjects.Email;
import com.acamus.telegrm.core.domain.valueobjects.Password;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private boolean enabled;

    private LocalDateTime createdAt;

    private LocalDateTime lastLoginAt;

    // --- MAPPERS ---

    public static UserEntity fromDomain(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setName(user.getName());
        entity.setEmail(user.getEmail().value());
        entity.setPassword(user.getPassword().value());
        entity.setEnabled(user.isEnabled());
        entity.setCreatedAt(user.getCreatedAt());
        entity.setLastLoginAt(user.getLastLoginAt());
        return entity;
    }

    public User toDomain() {
        return User.reconstruct(
                this.id,
                this.name,
                new Email(this.email),
                new Password(this.password),
                this.enabled,
                this.createdAt,
                this.lastLoginAt
        );
    }
}
