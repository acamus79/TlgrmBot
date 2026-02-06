package com.acamus.telegrm.infrastructure.adapters.out.persistence.jpa.entities;

import com.acamus.telegrm.core.domain.model.Conversation;
import com.acamus.telegrm.core.domain.valueobjects.TelegramChatId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "conversations")
@Getter
@Setter
@NoArgsConstructor
public class ConversationEntity {

    @Id
    private String id;

    @Column(name = "telegram_chat_id", unique = true, nullable = false)
    private Long telegramChatId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String username;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_message_at", nullable = false)
    private LocalDateTime lastMessageAt;

    public static ConversationEntity fromDomain(Conversation conversation) {
        ConversationEntity entity = new ConversationEntity();
        entity.setId(conversation.getId());
        entity.setTelegramChatId(conversation.getTelegramChatId().value());
        entity.setFirstName(conversation.getFirstName());
        entity.setLastName(conversation.getLastName());
        entity.setUsername(conversation.getUsername());
        entity.setCreatedAt(conversation.getCreatedAt());
        entity.setLastMessageAt(conversation.getLastMessageAt());
        return entity;
    }

    public Conversation toDomain() {
        return Conversation.reconstruct(
                this.id,
                new TelegramChatId(this.telegramChatId),
                this.firstName,
                this.lastName,
                this.username,
                this.createdAt,
                this.lastMessageAt
        );
    }
}
