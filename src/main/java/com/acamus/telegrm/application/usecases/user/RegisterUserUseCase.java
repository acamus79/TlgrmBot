package com.acamus.telegrm.application.usecases.user;

import com.acamus.telegrm.core.domain.exception.EmailAlreadyExistsException;
import com.acamus.telegrm.core.domain.model.User;
import com.acamus.telegrm.core.domain.valueobjects.Email;
import com.acamus.telegrm.core.domain.valueobjects.Password;
import com.acamus.telegrm.core.ports.in.user.RegisterUserCommand;
import com.acamus.telegrm.core.ports.in.user.RegisterUserPort;
import com.acamus.telegrm.core.ports.out.user.UserRepositoryPort;
import com.acamus.telegrm.infrastructure.adapters.out.security.PasswordHasher;

public class RegisterUserUseCase implements RegisterUserPort {

    private final UserRepositoryPort userRepository;
    private final PasswordHasher passwordHasher;

    public RegisterUserUseCase(UserRepositoryPort userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public User register(RegisterUserCommand command) {
        Email email = new Email(command.email());

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(command.email());
        }

        String hashedPassword = passwordHasher.hash(command.password());
        Password password = new Password(hashedPassword);

        return userRepository.save(User.create(command.name(), email, password));
    }
}
