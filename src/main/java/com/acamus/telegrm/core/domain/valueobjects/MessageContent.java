package com.acamus.telegrm.core.domain.valueobjects;

import java.util.Objects;

public record MessageContent(String value) {
    private static final int MAX_LENGTH = 4096; // Límite de Telegram

    public MessageContent {
        Objects.requireNonNull(value, "Message content cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Message content cannot be blank");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Message content exceeds max length of " + MAX_LENGTH);
        }
    }
}
