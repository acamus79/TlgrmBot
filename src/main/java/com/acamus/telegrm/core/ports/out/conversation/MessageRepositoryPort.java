package com.acamus.telegrm.core.ports.out.conversation;

import com.acamus.telegrm.core.domain.model.Message;
import java.util.List;

public interface MessageRepositoryPort {
    Message save(Message message);
    List<Message> findByConversationId(String conversationId);
}
