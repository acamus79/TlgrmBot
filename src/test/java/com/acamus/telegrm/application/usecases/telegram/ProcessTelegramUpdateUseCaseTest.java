package com.acamus.telegrm.application.usecases.telegram;

import com.acamus.telegrm.core.domain.model.Conversation;
import com.acamus.telegrm.core.domain.model.Message;
import com.acamus.telegrm.core.domain.valueobjects.TelegramChatId;
import com.acamus.telegrm.core.ports.in.telegram.ProcessUpdateCommand;
import com.acamus.telegrm.core.ports.out.ai.AiGeneratorPort;
import com.acamus.telegrm.core.ports.out.conversation.ConversationRepositoryPort;
import com.acamus.telegrm.core.ports.out.conversation.MessageRepositoryPort;
import com.acamus.telegrm.core.ports.out.telegram.TelegramPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessTelegramUpdateUseCaseTest {

    @Mock
    private ConversationRepositoryPort conversationRepository;

    @Mock
    private MessageRepositoryPort messageRepository;

    @Mock
    private TelegramPort telegramPort;

    @Mock
    private AiGeneratorPort aiGeneratorPort;

    @InjectMocks
    private ProcessTelegramUpdateUseCase useCase;

    private ProcessUpdateCommand command;

    @BeforeEach
    void setUp() {
        command = new ProcessUpdateCommand(
                12345L,
                "Hello",
                "John",
                "Doe",
                "johndoe"
        );
    }

    @Test
    @DisplayName("GIVEN a new message WHEN processing THEN a new conversation is created and a response is sent")
    void processUpdate_forNewUser_shouldCreateConversationAndSendResponse() {
        // GIVEN: El repositorio no encuentra una conversación existente
        when(conversationRepository.findByTelegramChatId(12345L)).thenReturn(Optional.empty());

        // GIVEN: El repositorio devuelve la conversación recién creada cuando se le pide que la guarde
        ArgumentCaptor<Conversation> conversationCaptor = ArgumentCaptor.forClass(Conversation.class);
        when(conversationRepository.save(conversationCaptor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        // GIVEN: La IA devuelve una respuesta
        when(aiGeneratorPort.generateResponse(anyString())).thenReturn("AI Response");

        // WHEN: Se procesa el comando
        useCase.processUpdate(command);

        // THEN: Se debe verificar que se intentó guardar una nueva conversación
        verify(conversationRepository, times(2)).save(any(Conversation.class)); // Una para crear, otra para actualizar
        
        // THEN: Se deben guardar dos mensajes (entrante y saliente)
        verify(messageRepository, times(2)).save(any(Message.class));

        // THEN: Se debe llamar a la IA
        verify(aiGeneratorPort).generateResponse(anyString());

        // THEN: Se debe enviar un mensaje de respuesta
        verify(telegramPort, times(1)).sendMessage(eq(12345L), anyString());

        // THEN: Validamos los detalles de la conversación creada
        Conversation savedConversation = conversationCaptor.getValue();
        assertNotNull(savedConversation.getId());
        assertEquals(12345L, savedConversation.getTelegramChatId().value());
        assertEquals("John", savedConversation.getFirstName());
    }

    @Test
    @DisplayName("GIVEN a message from an existing conversation WHEN processing THEN the existing conversation is used")
    void processUpdate_forExistingUser_shouldUseExistingConversation() {
        // GIVEN: El repositorio DEVUELVE una conversación existente
        Conversation existingConversation = Conversation.create(new TelegramChatId(12345L), "John", "Doe", "johndoe");
        when(conversationRepository.findByTelegramChatId(12345L)).thenReturn(Optional.of(existingConversation));

        // GIVEN: La IA devuelve una respuesta
        when(aiGeneratorPort.generateResponse(anyString())).thenReturn("AI Response");

        // WHEN: Se procesa el comando
        useCase.processUpdate(command);

        // THEN: Se debe guardar la conversación UNA SOLA VEZ (para actualizar lastMessageAt)
        verify(conversationRepository, times(1)).save(any(Conversation.class));
        
        // THEN: Se deben guardar dos mensajes
        verify(messageRepository, times(2)).save(any(Message.class));

        // THEN: Se debe enviar una respuesta
        verify(telegramPort, times(1)).sendMessage(eq(12345L), anyString());
    }

    @Test
    @DisplayName("GIVEN an update without a text message WHEN processing THEN no action is taken")
    void processUpdate_withNoTextMessage_shouldDoNothing() {
        // GIVEN: Un comando sin texto
        ProcessUpdateCommand commandWithNoText = new ProcessUpdateCommand(12345L, null, "John", "Doe", "johndoe");

        // WHEN: Se procesa el comando
        useCase.processUpdate(commandWithNoText);

        // THEN: No debe haber ninguna interacción con los repositorios ni con el puerto de Telegram
        verifyNoInteractions(conversationRepository);
        verifyNoInteractions(messageRepository);
        verifyNoInteractions(telegramPort);
        verifyNoInteractions(aiGeneratorPort);
    }
}
