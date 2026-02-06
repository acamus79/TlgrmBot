package com.acamus.telegrm.infrastructure.adapters.out.security;

import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordHasher {

    private final SCryptPasswordEncoder encoder =
            new SCryptPasswordEncoder(16384, 8, 1, 32, 16);

    public String hash(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    public boolean verify(String rawPassword, String hashedPassword) {
        return encoder.matches(rawPassword, hashedPassword);
    }
}
