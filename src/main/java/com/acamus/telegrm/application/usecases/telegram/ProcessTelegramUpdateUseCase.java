package com.acamus.telegrm.application.usecases.telegram;

import com.acamus.telegrm.core.domain.model.Conversation;
import com.acamus.telegrm.core.domain.model.Message;
import com.acamus.telegrm.core.domain.valueobjects.MessageContent;
import com.acamus.telegrm.core.domain.valueobjects.TelegramChatId;
import com.acamus.telegrm.core.ports.in.telegram.ProcessUpdateCommand;
import com.acamus.telegrm.core.ports.in.telegram.ProcessTelegramUpdatePort;
import com.acamus.telegrm.core.ports.out.ai.AiGeneratorPort;
import com.acamus.telegrm.core.ports.out.conversation.ConversationRepositoryPort;
import com.acamus.telegrm.core.ports.out.conversation.MessageRepositoryPort;
import com.acamus.telegrm.core.ports.out.telegram.TelegramPort;

public class ProcessTelegramUpdateUseCase implements ProcessTelegramUpdatePort {

    private final ConversationRepositoryPort conversationRepository;
    private final MessageRepositoryPort messageRepository;
    private final TelegramPort telegramPort;
    private final AiGeneratorPort aiGeneratorPort;

    public ProcessTelegramUpdateUseCase(
            ConversationRepositoryPort conversationRepository, 
            MessageRepositoryPort messageRepository, 
            TelegramPort telegramPort, 
            AiGeneratorPort aiGeneratorPort) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.telegramPort = telegramPort;
        this.aiGeneratorPort = aiGeneratorPort;
    }

    @Override
    public void processUpdate(ProcessUpdateCommand command) {
        if (command.text() == null || command.text().isBlank()) {
            return;
        }

        long chatIdValue = command.chatId();

        Conversation conversation = conversationRepository.findByTelegramChatId(chatIdValue)
                .orElseGet(() -> {
                    Conversation newConv = Conversation.create(
                        new TelegramChatId(chatIdValue),
                        command.firstName(), command.lastName(), command.username()
                    );
                    return conversationRepository.save(newConv);
                });

        Message incomingMessage = Message.createIncoming(conversation.getId(), new MessageContent(command.text()));
        messageRepository.save(incomingMessage);

        String responseText = aiGeneratorPort.generateResponse(command.text());
        telegramPort.sendMessage(chatIdValue, responseText);

        Message outgoingMessage = Message.createOutgoing(conversation.getId(), new MessageContent(responseText));
        messageRepository.save(outgoingMessage);
        
        conversation.updateLastMessageAt();
        conversationRepository.save(conversation);
    }
}
