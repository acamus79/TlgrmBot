package com.acamus.telegrm.infrastructure.adapters.out.telegram;

import com.acamus.telegrm.core.ports.out.telegram.TelegramPort;
import com.acamus.telegrm.infrastructure.adapters.out.telegram.dto.GetUpdatesResponse;
import com.acamus.telegrm.infrastructure.adapters.out.telegram.dto.Update;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;

@Component
public class TelegramAdapter implements TelegramPort {

    private final RestClient telegramRestClient;

    public TelegramAdapter(@Qualifier("telegramRestClient") RestClient telegramRestClient) {
        this.telegramRestClient = telegramRestClient;
    }

    @Override
    public List<Update> getUpdates(long offset) {
        GetUpdatesResponse response = telegramRestClient.get()
                .uri("/getUpdates?offset={offset}", offset)
                .retrieve()
                .body(GetUpdatesResponse.class);

        return (response != null && response.ok()) ? response.result() : Collections.emptyList();
    }

    @Override
    public void sendMessage(long chatId, String text) {
        telegramRestClient.get()
                .uri("/sendMessage?chat_id={chatId}&text={text}", chatId, text)
                .retrieve()
                .toBodilessEntity();
    }
}
