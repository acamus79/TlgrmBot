package com.acamus.telegrm.infrastructure.adapters.out.persistence;

import com.acamus.telegrm.core.domain.model.Message;
import com.acamus.telegrm.core.ports.out.conversation.MessageRepositoryPort;
import com.acamus.telegrm.infrastructure.adapters.out.persistence.jpa.entities.MessageEntity;
import com.acamus.telegrm.infrastructure.adapters.out.persistence.jpa.repositories.MessageJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MessageRepositoryAdapter implements MessageRepositoryPort {

    private final MessageJpaRepository jpaRepository;

    @Override
    public Message save(Message message) {
        MessageEntity entity = MessageEntity.fromDomain(message);
        return jpaRepository.save(entity).toDomain();
    }

    @Override
    public List<Message> findByConversationId(String conversationId) {
        return jpaRepository.findByConversationIdOrderBySentAtAsc(conversationId).stream()
                .map(MessageEntity::toDomain)
                .collect(Collectors.toList());
    }
}
