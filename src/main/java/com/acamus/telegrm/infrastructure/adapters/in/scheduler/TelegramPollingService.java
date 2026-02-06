package com.acamus.telegrm.infrastructure.adapters.in.scheduler;

import com.acamus.telegrm.core.ports.in.telegram.ProcessUpdateCommand;
import com.acamus.telegrm.core.ports.in.telegram.ProcessTelegramUpdatePort;
import com.acamus.telegrm.core.ports.out.telegram.TelegramPort;
import com.acamus.telegrm.infrastructure.adapters.out.telegram.dto.MessageDto;
import com.acamus.telegrm.infrastructure.adapters.out.telegram.dto.Update;
import com.acamus.telegrm.infrastructure.adapters.out.telegram.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramPollingService {

    private final TelegramPort telegramPort;
    private final ProcessTelegramUpdatePort processTelegramUpdatePort;
    private long lastUpdateId = 0;

    @Scheduled(fixedRate = 5000)
    public void pollForUpdates() {
        log.info("Polling for new messages...");
        List<Update> updates = telegramPort.getUpdates(lastUpdateId + 1);

        if (!updates.isEmpty()) {
            log.info("Received {} new updates.", updates.size());
            for (Update update : updates) {
                if (update.message() != null && update.message().from() != null) {
                    // Conversión de DTO a Command
                    MessageDto messageDto = update.message();
                    UserDto userDto = messageDto.from();
                    ProcessUpdateCommand command = new ProcessUpdateCommand(
                            messageDto.chat().id(),
                            messageDto.text(),
                            userDto.firstName(),
                            userDto.lastName(),
                            userDto.username()
                    );
                    processTelegramUpdatePort.processUpdate(command);
                }
                lastUpdateId = update.updateId();
            }
        }
    }
}
