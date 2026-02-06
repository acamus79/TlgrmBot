package com.acamus.telegrm.application.usecases.conversation;

import com.acamus.telegrm.core.domain.model.Message;
import com.acamus.telegrm.core.ports.in.conversation.GetMessagesByConversationPort;
import com.acamus.telegrm.core.ports.out.conversation.MessageRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class GetMessagesByConversationUseCase implements GetMessagesByConversationPort {
    private final MessageRepositoryPort messageRepository;

    @Override
    public List<Message> getMessages(String conversationId) {
        // Aquí podríamos añadir lógica para verificar si la conversación existe primero
        return messageRepository.findByConversationId(conversationId);
    }
}
