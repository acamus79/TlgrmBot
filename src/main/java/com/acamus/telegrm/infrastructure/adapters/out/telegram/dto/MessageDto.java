package com.acamus.telegrm.infrastructure.adapters.out.telegram.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MessageDto(
    @JsonProperty("message_id") long messageId,
    UserDto from,
    ChatDto chat,
    long date,
    String text
) {}
