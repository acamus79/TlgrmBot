package com.acamus.telegrm.core.domain.valueobjects;

import java.util.Objects;

public record Password(String value) {
    public Password {
        Objects.requireNonNull(value, "Password cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Password cannot be blank");
        }
    }
}
