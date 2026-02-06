package com.acamus.telegrm.infrastructure.adapters.in.web.conversation.dto;

import java.time.LocalDateTime;

public record ConversationSummaryDto(
    String id,
    String participantName,
    LocalDateTime lastMessageAt
) {}
