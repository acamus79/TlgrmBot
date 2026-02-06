package com.acamus.telegrm.core.domain.valueobjects;

import java.util.Objects;

public record TelegramChatId(Long value) {
    public TelegramChatId {
        Objects.requireNonNull(value, "Telegram Chat ID cannot be null");
    }
}
