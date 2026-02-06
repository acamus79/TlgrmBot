package com.acamus.telegrm.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class OpenRouterConfig {

    @Value("${openrouter.base-url}")
    private String baseUrl;

    @Value("${openrouter.api-key}")
    private String apiKey;

    @Bean
    public RestClient openRouterRestClient() {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("HTTP-Referer", "https://github.com/acamus/telegrm")
                .defaultHeader("X-Title", "Telegrm Bot")
                .build();
    }
}
