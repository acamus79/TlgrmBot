package com.acamus.telegrm.application.usecases.conversation;

import com.acamus.telegrm.core.domain.model.Conversation;
import com.acamus.telegrm.core.domain.model.Message;
import com.acamus.telegrm.core.domain.valueobjects.MessageContent;
import com.acamus.telegrm.core.ports.in.conversation.SendMessageCommand;
import com.acamus.telegrm.core.ports.in.conversation.SendMessagePort;
import com.acamus.telegrm.core.ports.out.conversation.ConversationRepositoryPort;
import com.acamus.telegrm.core.ports.out.conversation.MessageRepositoryPort;
import com.acamus.telegrm.core.ports.out.telegram.TelegramPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SendMessageUseCase implements SendMessagePort {

    private final ConversationRepositoryPort conversationRepository;
    private final MessageRepositoryPort messageRepository;
    private final TelegramPort telegramPort;

    @Override
    public void sendMessage(SendMessageCommand command) {
        // 1. Validar que la conversación existe
        Conversation conversation = conversationRepository.findById(command.conversationId())
                .orElseThrow(() -> new RuntimeException("Conversation not found with ID: " + command.conversationId()));

        // 2. Crear y guardar el mensaje saliente
        Message outgoingMessage = Message.createOutgoing(conversation.getId(), new MessageContent(command.content()));
        messageRepository.save(outgoingMessage);

        // 3. Enviar el mensaje a través de Telegram
        telegramPort.sendMessage(conversation.getTelegramChatId().value(), command.content());

        // 4. Actualizar la fecha del último mensaje en la conversación
        conversation.updateLastMessageAt();
        conversationRepository.save(conversation);
    }
}
