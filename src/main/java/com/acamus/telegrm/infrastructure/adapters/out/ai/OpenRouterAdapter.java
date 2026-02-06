package com.acamus.telegrm.infrastructure.adapters.out.ai;

import com.acamus.telegrm.core.ports.out.ai.AiGeneratorPort;
import com.acamus.telegrm.infrastructure.adapters.out.ai.dto.openrouter.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;

@Component
public class OpenRouterAdapter implements AiGeneratorPort {

    private final RestClient restClient;
    private final String model;
    private final String systemPrompt;
    private final double temperature;
    private final int maxTokens;

    public OpenRouterAdapter(
            @Qualifier("openRouterRestClient") RestClient restClient,
            @Value("${openrouter.model}") String model,
            @Value("${openrouter.system-prompt}") String systemPrompt,
            @Value("${openrouter.temperature}") double temperature,
            @Value("${openrouter.max-tokens}") int maxTokens) {
        this.restClient = restClient;
        this.model = model;
        this.systemPrompt = systemPrompt;
        this.temperature = temperature;
        this.maxTokens = maxTokens;
    }

    @Override
    public String generateResponse(String userInput) {
        try {
            ChatMessage systemMessage = new ChatMessage("system", systemPrompt, null);
            ChatMessage userMessage = new ChatMessage("user", userInput, null);

            OpenRouterRequest request = new OpenRouterRequest(model, List.of(systemMessage, userMessage), maxTokens, temperature);

            OpenRouterResponse response = restClient.post()
                    .uri("/chat/completions")
                    .body(request)
                    .retrieve()
                    .body(OpenRouterResponse.class);

            if (response != null && response.choices() != null && !response.choices().isEmpty()) {
                ChatMessage msg = response.choices().getFirst().message();
                
                // NOTA: Descomentar para ver el razonamiento si el modelo lo soporta (reasoning) sobre la respuesta formal
//                if (msg.reasoning() != null && !msg.reasoning().isBlank()) {
//                    return msg.reasoning();
//                }
                
                return msg.content();
            }
        } catch (RestClientResponseException e) {
            return "La IA rechazó mi petición (HTTP " + e.getStatusCode() + ").";
        } catch (ResourceAccessException e) {
            return "No puedo conectar con la IA (Error de red).";
        } catch (Exception e) {
            return "Ocurrió un error interno al procesar la respuesta de la IA: " + e.getMessage();
        }

        return "El silencio del vacío cósmico me responde.";
    }
}
