package com.acamus.telegrm.application.usecases.user;

import com.acamus.telegrm.core.domain.exception.EmailAlreadyExistsException;
import com.acamus.telegrm.core.domain.model.User;
import com.acamus.telegrm.core.domain.valueobjects.Email;
import com.acamus.telegrm.core.ports.in.user.RegisterUserCommand;
import com.acamus.telegrm.core.ports.out.user.UserRepositoryPort;
import com.acamus.telegrm.infrastructure.adapters.out.security.PasswordHasher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private PasswordHasher passwordHasher;

    @InjectMocks
    private RegisterUserUseCase useCase;

    @Test
    @DisplayName("GIVEN valid command WHEN registering THEN password is hashed and user is saved")
    void register_validCommand_shouldHashPasswordAndSaveUser() {
        // GIVEN
        RegisterUserCommand command = new RegisterUserCommand("John", "john@example.com", "password123");
        when(userRepository.existsByEmail(any(Email.class))).thenReturn(false);
        when(passwordHasher.hash("password123")).thenReturn("hashed_secret");
        
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // WHEN
        User savedUser = useCase.register(command);

        // THEN
        assertNotNull(savedUser);
        assertEquals("hashed_secret", savedUser.getPassword().value());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("GIVEN existing email WHEN registering THEN exception is thrown")
    void register_existingEmail_shouldThrowException() {
        // GIVEN
        RegisterUserCommand command = new RegisterUserCommand("John", "john@example.com", "password123");
        when(userRepository.existsByEmail(any(Email.class))).thenReturn(true);

        // WHEN & THEN
        assertThrows(EmailAlreadyExistsException.class, () -> useCase.register(command));
        
        verify(userRepository, never()).save(any(User.class));
    }
}
