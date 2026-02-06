package com.acamus.telegrm.core.ports.out.conversation;

import com.acamus.telegrm.core.domain.model.Conversation;
import java.util.List;
import java.util.Optional;

public interface ConversationRepositoryPort {
    Conversation save(Conversation conversation);
    Optional<Conversation> findByTelegramChatId(Long telegramChatId);
    Optional<Conversation> findById(String id);
    List<Conversation> findAll();
}
