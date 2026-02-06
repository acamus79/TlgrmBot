package com.acamus.telegrm.infrastructure.adapters.out.persistence.jpa.entities;

import com.acamus.telegrm.core.domain.model.Message;
import com.acamus.telegrm.core.domain.valueobjects.MessageContent;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
public class MessageEntity {

    @Id
    private String id;

    @Column(name = "conversation_id", nullable = false)
    private String conversationId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Message.MessageDirection direction;

    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt;

    public static MessageEntity fromDomain(Message message) {
        MessageEntity entity = new MessageEntity();
        entity.setId(message.getId());
        entity.setConversationId(message.getConversationId());
        entity.setContent(message.getContent().value());
        entity.setDirection(message.getDirection());
        entity.setSentAt(message.getSentAt());
        return entity;
    }

    public Message toDomain() {
        return Message.reconstruct(
                this.id,
                this.conversationId,
                new MessageContent(this.content),
                this.direction,
                this.sentAt
        );
    }
}
