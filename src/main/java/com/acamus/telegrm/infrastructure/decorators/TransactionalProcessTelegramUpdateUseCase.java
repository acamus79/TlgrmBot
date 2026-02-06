package com.acamus.telegrm.infrastructure.decorators;

import com.acamus.telegrm.core.ports.in.telegram.ProcessUpdateCommand;
import com.acamus.telegrm.core.ports.in.telegram.ProcessTelegramUpdatePort;
import org.springframework.transaction.annotation.Transactional;

// This is the decorator. Its only responsibility is to add the transaction.
public class TransactionalProcessTelegramUpdateUseCase implements ProcessTelegramUpdatePort {

    private final ProcessTelegramUpdatePort actualUseCase;

    public TransactionalProcessTelegramUpdateUseCase(ProcessTelegramUpdatePort actualUseCase) {
        this.actualUseCase = actualUseCase;
    }

    @Override
    @Transactional
    public void processUpdate(ProcessUpdateCommand command) {
        // Delegates execution to the actual use case, but within a transactional context.
        actualUseCase.processUpdate(command);
    }
}
