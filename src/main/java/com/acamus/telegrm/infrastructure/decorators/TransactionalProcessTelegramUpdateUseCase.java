package com.acamus.telegrm.infrastructure.decorators;

import com.acamus.telegrm.core.ports.in.telegram.ProcessUpdateCommand;
import com.acamus.telegrm.core.ports.in.telegram.ProcessTelegramUpdatePort;
import org.springframework.transaction.annotation.Transactional;

// Este es el decorador. Su única responsabilidad es añadir la transacción.
public class TransactionalProcessTelegramUpdateUseCase implements ProcessTelegramUpdatePort {

    private final ProcessTelegramUpdatePort actualUseCase;

    public TransactionalProcessTelegramUpdateUseCase(ProcessTelegramUpdatePort actualUseCase) {
        this.actualUseCase = actualUseCase;
    }

    @Override
    @Transactional
    public void processUpdate(ProcessUpdateCommand command) {
        // Delega la ejecución al caso de uso real, pero dentro de un contexto transaccional.
        actualUseCase.processUpdate(command);
    }
}
