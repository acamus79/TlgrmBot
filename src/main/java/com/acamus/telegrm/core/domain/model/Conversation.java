package com.acamus.telegrm.core.domain.model;

import com.acamus.telegrm.core.domain.valueobjects.TelegramChatId;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Conversation {

    private String id;
    private TelegramChatId telegramChatId;
    private String firstName;
    private String lastName;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime lastMessageAt;

    private Conversation() {}

    public static Conversation create(TelegramChatId telegramChatId, String firstName, String lastName, String username) {
        Conversation conv = new Conversation();
        conv.id = UUID.randomUUID().toString();
        conv.telegramChatId = Objects.requireNonNull(telegramChatId);
        conv.firstName = firstName;
        conv.lastName = lastName;
        conv.username = username;
        conv.createdAt = LocalDateTime.now();
        conv.lastMessageAt = LocalDateTime.now();
        return conv;
    }

    public void updateLastMessageAt() {
        this.lastMessageAt = LocalDateTime.now();
    }

    // Reconstrucción
    public static Conversation reconstruct(String id, TelegramChatId telegramChatId, String firstName, String lastName, String username, LocalDateTime createdAt, LocalDateTime lastMessageAt) {
        Conversation conv = new Conversation();
        conv.id = id;
        conv.telegramChatId = telegramChatId;
        conv.firstName = firstName;
        conv.lastName = lastName;
        conv.username = username;
        conv.createdAt = createdAt;
        conv.lastMessageAt = lastMessageAt;
        return conv;
    }

    // Getters
    public String getId() { return id; }
    public TelegramChatId getTelegramChatId() { return telegramChatId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getUsername() { return username; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getLastMessageAt() { return lastMessageAt; }
}
