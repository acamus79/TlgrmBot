package com.acamus.telegrm.core.ports.out.telegram;

import com.acamus.telegrm.infrastructure.adapters.out.telegram.dto.Update;
import java.util.List;

public interface TelegramPort {
    List<Update> getUpdates(long offset);
    void sendMessage(long chatId, String text);
}
