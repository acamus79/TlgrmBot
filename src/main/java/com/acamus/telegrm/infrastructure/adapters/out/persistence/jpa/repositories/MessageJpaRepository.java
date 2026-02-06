package com.acamus.telegrm.infrastructure.adapters.out.persistence.jpa.repositories;

import com.acamus.telegrm.infrastructure.adapters.out.persistence.jpa.entities.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageJpaRepository extends JpaRepository<MessageEntity, String> {
    List<MessageEntity> findByConversationIdOrderBySentAtAsc(String conversationId);
}
