package com.acamus.telegrm.application.usecases.user;

import com.acamus.telegrm.core.domain.exception.InvalidCredentialsException;
import com.acamus.telegrm.core.domain.model.User;
import com.acamus.telegrm.core.domain.valueobjects.Email;
import com.acamus.telegrm.core.ports.in.user.AuthenticateUserCommand;
import com.acamus.telegrm.core.ports.in.user.AuthenticateUserPort;
import com.acamus.telegrm.core.ports.out.security.TokenGeneratorPort;
import com.acamus.telegrm.core.ports.out.user.UserRepositoryPort;
import com.acamus.telegrm.infrastructure.adapters.out.security.PasswordHasher;

public class AuthenticateUserUseCase implements AuthenticateUserPort {

    private final UserRepositoryPort userRepository;
    private final PasswordHasher passwordHasher;
    private final TokenGeneratorPort tokenGenerator;

    public AuthenticateUserUseCase(UserRepositoryPort userRepository, 
                                   PasswordHasher passwordHasher,
                                   TokenGeneratorPort tokenGenerator) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public String authenticate(AuthenticateUserCommand command) {
        Email email = new Email(command.email());

        User user = userRepository.findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordHasher.verify(command.password(), user.getPassword().value())) {
            throw new InvalidCredentialsException();
        }

        return tokenGenerator.generateToken(user);
    }
}
