package com.acamus.telegrm.application.usecases.conversation;

import com.acamus.telegrm.core.domain.model.Conversation;
import com.acamus.telegrm.core.domain.model.Message;
import com.acamus.telegrm.core.domain.valueobjects.TelegramChatId;
import com.acamus.telegrm.core.ports.in.conversation.SendMessageCommand;
import com.acamus.telegrm.core.ports.out.conversation.ConversationRepositoryPort;
import com.acamus.telegrm.core.ports.out.conversation.MessageRepositoryPort;
import com.acamus.telegrm.core.ports.out.telegram.TelegramPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SendMessageUseCaseTest {

    @Mock
    private ConversationRepositoryPort conversationRepository;

    @Mock
    private MessageRepositoryPort messageRepository;

    @Mock
    private TelegramPort telegramPort;

    @InjectMocks
    private SendMessageUseCase useCase;

    @Test
    @DisplayName("GIVEN a valid command WHEN sending message THEN message is saved and sent via Telegram")
    void sendMessage_validCommand_shouldSaveAndSend() {
        // GIVEN
        String conversationId = "conv-123";
        long telegramChatId = 98765L;
        String content = "Hello from admin";
        SendMessageCommand command = new SendMessageCommand(conversationId, content);

        Conversation conversation = Conversation.create(new TelegramChatId(telegramChatId), "John", "Doe", "johndoe");
        when(conversationRepository.findById(conversationId)).thenReturn(Optional.of(conversation));

        // WHEN
        useCase.sendMessage(command);

        // THEN
        verify(messageRepository).save(any(Message.class));
        verify(telegramPort).sendMessage(eq(telegramChatId), eq(content));
        verify(conversationRepository).save(conversation); // Update lastMessageAt
    }

    @Test
    @DisplayName("GIVEN a non-existent conversation ID WHEN sending message THEN exception is thrown")
    void sendMessage_nonExistentConversation_shouldThrowException() {
        // GIVEN
        String conversationId = "non-existent-id";
        SendMessageCommand command = new SendMessageCommand(conversationId, "Hello");

        when(conversationRepository.findById(conversationId)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(RuntimeException.class, () -> useCase.sendMessage(command));
        
        verifyNoInteractions(messageRepository);
        verifyNoInteractions(telegramPort);
    }
}
