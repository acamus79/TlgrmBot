package com.acamus.telegrm.infrastructure.adapters.out.persistence;

import com.acamus.telegrm.core.domain.model.User;
import com.acamus.telegrm.core.domain.valueobjects.Email;
import com.acamus.telegrm.core.ports.out.user.UserRepositoryPort;
import com.acamus.telegrm.infrastructure.adapters.out.persistence.jpa.entities.UserEntity;
import com.acamus.telegrm.infrastructure.adapters.out.persistence.jpa.repositories.UserJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserJpaRepository jpaRepository;

    public UserRepositoryAdapter(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public User save(User user) {
        UserEntity entity = UserEntity.fromDomain(user);
        UserEntity saved = jpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public Optional<User> findById(String id) {
        return jpaRepository.findById(id).map(UserEntity::toDomain);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return jpaRepository.findByEmail(email.value()).map(UserEntity::toDomain);
    }

    @Override
    public boolean existsByEmail(Email email) {
        return jpaRepository.existsByEmail(email.value());
    }

    @Override
    public List<User> findAll() {
        return jpaRepository.findAll().stream()
                .map(UserEntity::toDomain)
                .collect(Collectors.toList());
    }
}
