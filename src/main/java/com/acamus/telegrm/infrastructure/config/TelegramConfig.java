package com.acamus.telegrm.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class TelegramConfig {

    @Value("${telegram.bot-token}")
    private String botToken;

    @Bean
    public RestClient telegramRestClient() {
        String baseUrl = "https://api.telegram.org/bot" + botToken;
        return RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}
