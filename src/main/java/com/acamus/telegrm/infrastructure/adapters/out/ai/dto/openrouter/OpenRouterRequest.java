package com.acamus.telegrm.infrastructure.adapters.out.ai.dto.openrouter;

import java.util.List;

public record OpenRouterRequest(String model, List<ChatMessage> messages, int maxTokens, double temperature) {}
