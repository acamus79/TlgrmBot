package com.acamus.telegrm.core.ports.in.telegram;

public interface ProcessTelegramUpdatePort {
    void processUpdate(ProcessUpdateCommand command);
}
