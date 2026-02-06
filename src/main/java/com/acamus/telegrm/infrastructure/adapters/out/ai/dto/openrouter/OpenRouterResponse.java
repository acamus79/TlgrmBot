package com.acamus.telegrm.infrastructure.adapters.out.ai.dto.openrouter;

import java.util.List;

public record OpenRouterResponse(List<Choice> choices) {}
