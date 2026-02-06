package com.acamus.telegrm.core.domain.model;

import com.acamus.telegrm.core.domain.valueobjects.MessageContent;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Message {

    private String id;
    private String conversationId;
    private MessageContent content;
    private MessageDirection direction;
    private LocalDateTime sentAt;

    private Message() {}

    public static Message createIncoming(String conversationId, MessageContent content) {
        return create(conversationId, content, MessageDirection.INCOMING);
    }

    public static Message createOutgoing(String conversationId, MessageContent content) {
        return create(conversationId, content, MessageDirection.OUTGOING);
    }

    private static Message create(String conversationId, MessageContent content, MessageDirection direction) {
        Message msg = new Message();
        msg.id = UUID.randomUUID().toString();
        msg.conversationId = Objects.requireNonNull(conversationId);
        msg.content = Objects.requireNonNull(content);
        msg.direction = Objects.requireNonNull(direction);
        msg.sentAt = LocalDateTime.now();
        return msg;
    }
    
    // Reconstrucción desde BD
    public static Message reconstruct(String id, String conversationId, MessageContent content, MessageDirection direction, LocalDateTime sentAt) {
        Message msg = new Message();
        msg.id = id;
        msg.conversationId = conversationId;
        msg.content = content;
        msg.direction = direction;
        msg.sentAt = sentAt;
        return msg;
    }

    public enum MessageDirection {
        INCOMING, OUTGOING
    }

    // Getters
    public String getId() { return id; }
    public String getConversationId() { return conversationId; }
    public MessageContent getContent() { return content; }
    public MessageDirection getDirection() { return direction; }
    public LocalDateTime getSentAt() { return sentAt; }
}
