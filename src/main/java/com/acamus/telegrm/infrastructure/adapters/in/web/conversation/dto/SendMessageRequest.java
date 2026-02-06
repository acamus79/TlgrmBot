package com.acamus.telegrm.infrastructure.adapters.in.web.conversation.dto;

import jakarta.validation.constraints.NotBlank;

public record SendMessageRequest(@NotBlank(message = "Content cannot be blank") String content) {}
