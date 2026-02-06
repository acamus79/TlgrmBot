package com.acamus.telegrm.infrastructure.adapters.in.web.conversation;

import com.acamus.telegrm.core.domain.model.Conversation;
import com.acamus.telegrm.core.domain.model.Message;
import com.acamus.telegrm.core.ports.in.conversation.GetMessagesByConversationPort;
import com.acamus.telegrm.core.ports.in.conversation.ListConversationsPort;
import com.acamus.telegrm.core.ports.in.conversation.SendMessageCommand;
import com.acamus.telegrm.core.ports.in.conversation.SendMessagePort;
import com.acamus.telegrm.infrastructure.adapters.in.web.conversation.dto.ConversationSummaryDto;
import com.acamus.telegrm.infrastructure.adapters.in.web.conversation.dto.MessageDto;
import com.acamus.telegrm.infrastructure.adapters.in.web.conversation.dto.SendMessageRequest;
import com.acamus.telegrm.infrastructure.exceptions.GlobalExceptionHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
@Tag(name = "Conversations", description = "Endpoints para la gestión de conversaciones de Telegram.")
@PreAuthorize("isAuthenticated()")
public class ConversationController {

    private final ListConversationsPort listConversationsPort;
    private final GetMessagesByConversationPort getMessagesByConversationPort;
    private final SendMessagePort sendMessagePort;

    @Operation(summary = "Listar todas las conversaciones", description = "Devuelve una lista resumida de todas las conversaciones iniciadas con el bot.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de conversaciones obtenida", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ConversationSummaryDto.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado para acceder a este recurso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<ConversationSummaryDto>> listConversations() {
        List<Conversation> conversations = listConversationsPort.listAll();
        List<ConversationSummaryDto> dtos = conversations.stream()
                .map(this::toSummaryDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Obtener mensajes de una conversación", description = "Devuelve la lista completa de mensajes para una conversación específica, ordenados por fecha.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de mensajes obtenida", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDto.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado para acceder a este recurso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Conversación no encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    @GetMapping("/{id}/messages")
    public ResponseEntity<List<MessageDto>> getMessages(@PathVariable String id) {
        List<Message> messages = getMessagesByConversationPort.getMessages(id);
        List<MessageDto> dtos = messages.stream()
                .map(this::toMessageDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Enviar un mensaje a una conversación", description = "Permite a un usuario administrador enviar un mensaje proactivamente a un chat de Telegram.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Mensaje enviado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Conversación no encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    @PostMapping("/{id}/messages")
    public ResponseEntity<Void> sendMessage(@PathVariable String id, @Valid @RequestBody SendMessageRequest request) {
        SendMessageCommand command = new SendMessageCommand(id, request.content());
        sendMessagePort.sendMessage(command);
        return ResponseEntity.ok().build();
    }

    private ConversationSummaryDto toSummaryDto(Conversation conversation) {
        String participantName = conversation.getUsername() != null ? conversation.getUsername() : conversation.getFirstName();
        return new ConversationSummaryDto(
                conversation.getId(),
                participantName,
                conversation.getLastMessageAt()
        );
    }

    private MessageDto toMessageDto(Message message) {
        return new MessageDto(
                message.getId(),
                message.getContent().value(),
                message.getDirection(),
                message.getSentAt()
        );
    }
}
