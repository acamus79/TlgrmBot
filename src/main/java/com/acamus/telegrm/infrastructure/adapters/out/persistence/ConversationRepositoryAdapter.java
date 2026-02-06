package com.acamus.telegrm.infrastructure.adapters.out.persistence;

import com.acamus.telegrm.core.domain.model.Conversation;
import com.acamus.telegrm.core.ports.out.conversation.ConversationRepositoryPort;
import com.acamus.telegrm.infrastructure.adapters.out.persistence.jpa.entities.ConversationEntity;
import com.acamus.telegrm.infrastructure.adapters.out.persistence.jpa.repositories.ConversationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ConversationRepositoryAdapter implements ConversationRepositoryPort {

    private final ConversationJpaRepository jpaRepository;

    @Override
    public Conversation save(Conversation conversation) {
        ConversationEntity entity = ConversationEntity.fromDomain(conversation);
        return jpaRepository.save(entity).toDomain();
    }

    @Override
    public Optional<Conversation> findByTelegramChatId(Long telegramChatId) {
        return jpaRepository.findByTelegramChatId(telegramChatId)
                .map(ConversationEntity::toDomain);
    }

    @Override
    public Optional<Conversation> findById(String id) {
        return jpaRepository.findById(id)
                .map(ConversationEntity::toDomain);
    }

    @Override
    public List<Conversation> findAll() {
        return jpaRepository.findAll().stream()
                .map(ConversationEntity::toDomain)
                .collect(Collectors.toList());
    }
}
