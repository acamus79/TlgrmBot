package com.acamus.telegrm.infrastructure.adapters.out.ai.dto.openrouter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ChatMessage(
    String role,
    String content,
    String reasoning
) {}
