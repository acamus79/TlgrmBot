package com.acamus.telegrm.infrastructure.adapters.in.web.auth;

import com.acamus.telegrm.core.domain.model.User;
import com.acamus.telegrm.core.ports.in.user.AuthenticateUserCommand;
import com.acamus.telegrm.core.ports.in.user.AuthenticateUserPort;
import com.acamus.telegrm.core.ports.in.user.RegisterUserCommand;
import com.acamus.telegrm.core.ports.in.user.RegisterUserPort;
import com.acamus.telegrm.infrastructure.adapters.in.web.auth.dto.LoginRequest;
import com.acamus.telegrm.infrastructure.adapters.in.web.auth.dto.LoginResponse;
import com.acamus.telegrm.infrastructure.adapters.in.web.auth.dto.RegisterRequest;
import com.acamus.telegrm.infrastructure.adapters.in.web.auth.dto.RegisterResponse;
import com.acamus.telegrm.infrastructure.exceptions.GlobalExceptionHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints para registro y login de usuarios.")
public class AuthController {

    private final RegisterUserPort registerUserPort;
    private final AuthenticateUserPort authenticateUserPort;

    @Operation(summary = "Registrar un nuevo usuario", description = "Crea una nueva cuenta de usuario para acceder al panel de administración.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegisterResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        RegisterUserCommand command = new RegisterUserCommand(
            request.name(),
            request.email(),
            request.password()
        );

        User user = registerUserPort.register(command);

        RegisterResponse response = new RegisterResponse(
            user.getId(),
            user.getEmail().value(),
            user.getName()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Autenticar un usuario", description = "Genera un token JWT para un usuario existente a partir de su email y contraseña.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Autenticación exitosa", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))),
        @ApiResponse(responseCode = "401", description = "Credenciales inválidas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthenticateUserCommand command = new AuthenticateUserCommand(
            request.email(),
            request.password()
        );

        String token = authenticateUserPort.authenticate(command);

        return ResponseEntity.ok(new LoginResponse(token));
    }
}
