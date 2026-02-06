package com.acamus.telegrm.infrastructure.adapters.out.persistence.jpa.repositories;

import com.acamus.telegrm.infrastructure.adapters.out.persistence.jpa.entities.ConversationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConversationJpaRepository extends JpaRepository<ConversationEntity, String> {
    Optional<ConversationEntity> findByTelegramChatId(Long telegramChatId);
}
