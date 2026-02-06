package com.acamus.telegrm.infrastructure.decorators;

import com.acamus.telegrm.core.ports.in.conversation.SendMessageCommand;
import com.acamus.telegrm.core.ports.in.conversation.SendMessagePort;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class TransactionalSendMessageUseCase implements SendMessagePort {

    private final SendMessagePort actualUseCase;

    @Override
    @Transactional
    public void sendMessage(SendMessageCommand command) {
        actualUseCase.sendMessage(command);
    }
}
