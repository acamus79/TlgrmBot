package com.acamus.telegrm.core.ports.in.user;

public record RegisterUserCommand(
    String name,
    String email,
    String password
) {
    public RegisterUserCommand {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }
    }
}
