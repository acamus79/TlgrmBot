package com.acamus.telegrm.application.usecases.conversation;

import com.acamus.telegrm.core.domain.model.Conversation;
import com.acamus.telegrm.core.ports.in.conversation.ListConversationsPort;
import com.acamus.telegrm.core.ports.out.conversation.ConversationRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ListConversationsUseCase implements ListConversationsPort {
    private final ConversationRepositoryPort conversationRepository;

    @Override
    public List<Conversation> listAll() {
        return conversationRepository.findAll();
    }
}
