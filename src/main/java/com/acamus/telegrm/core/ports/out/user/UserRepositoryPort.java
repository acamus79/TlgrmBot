package com.acamus.telegrm.core.ports.out.user;

import com.acamus.telegrm.core.domain.model.User;
import com.acamus.telegrm.core.domain.valueobjects.Email;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findById(String id);
    Optional<User> findByEmail(Email email);
    boolean existsByEmail(Email email);
    List<User> findAll();
}
