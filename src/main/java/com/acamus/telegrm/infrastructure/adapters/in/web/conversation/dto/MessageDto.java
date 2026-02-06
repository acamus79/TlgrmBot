package com.acamus.telegrm.infrastructure.adapters.in.web.conversation.dto;

import com.acamus.telegrm.core.domain.model.Message;
import java.time.LocalDateTime;

public record MessageDto(
    String id,
    String content,
    Message.MessageDirection direction,
    LocalDateTime sentAt
) {}
