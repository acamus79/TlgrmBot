package com.acamus.telegrm.infrastructure.config;

import com.acamus.telegrm.application.usecases.conversation.GetMessagesByConversationUseCase;
import com.acamus.telegrm.application.usecases.conversation.ListConversationsUseCase;
import com.acamus.telegrm.application.usecases.conversation.SendMessageUseCase;
import com.acamus.telegrm.application.usecases.telegram.ProcessTelegramUpdateUseCase;
import com.acamus.telegrm.application.usecases.user.AuthenticateUserUseCase;
import com.acamus.telegrm.application.usecases.user.RegisterUserUseCase;
import com.acamus.telegrm.core.ports.in.conversation.GetMessagesByConversationPort;
import com.acamus.telegrm.core.ports.in.conversation.ListConversationsPort;
import com.acamus.telegrm.core.ports.in.conversation.SendMessagePort;
import com.acamus.telegrm.core.ports.in.telegram.ProcessTelegramUpdatePort;
import com.acamus.telegrm.core.ports.out.ai.AiGeneratorPort;
import com.acamus.telegrm.core.ports.out.conversation.ConversationRepositoryPort;
import com.acamus.telegrm.core.ports.out.conversation.MessageRepositoryPort;
import com.acamus.telegrm.core.ports.out.security.TokenGeneratorPort;
import com.acamus.telegrm.core.ports.out.telegram.TelegramPort;
import com.acamus.telegrm.core.ports.out.user.UserRepositoryPort;
import com.acamus.telegrm.infrastructure.adapters.out.security.PasswordHasher;
import com.acamus.telegrm.infrastructure.decorators.TransactionalProcessTelegramUpdateUseCase;
import com.acamus.telegrm.infrastructure.decorators.TransactionalSendMessageUseCase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class BeanConfiguration {

    @Bean
    public RegisterUserUseCase registerUserUseCase(UserRepositoryPort userRepository, PasswordHasher passwordHasher) {
        return new RegisterUserUseCase(userRepository, passwordHasher);
    }

    @Bean
    public AuthenticateUserUseCase authenticateUserUseCase(UserRepositoryPort userRepository, 
                                                           PasswordHasher passwordHasher,
                                                           TokenGeneratorPort tokenGenerator) {
        return new AuthenticateUserUseCase(userRepository, passwordHasher, tokenGenerator);
    }

    @Bean("processTelegramUpdateUseCase")
    public ProcessTelegramUpdatePort processTelegramUpdateUseCase(
            ConversationRepositoryPort conversationRepository,
            MessageRepositoryPort messageRepository,
            TelegramPort telegramPort,
            AiGeneratorPort aiGeneratorPort) {
        return new ProcessTelegramUpdateUseCase(conversationRepository, messageRepository, telegramPort, aiGeneratorPort);
    }

    @Bean
    @Primary
    public ProcessTelegramUpdatePort transactionalProcessTelegramUpdateUseCase(
            @Qualifier("processTelegramUpdateUseCase") ProcessTelegramUpdatePort useCase) {
        return new TransactionalProcessTelegramUpdateUseCase(useCase);
    }

    @Bean
    public ListConversationsPort listConversationsPort(ConversationRepositoryPort conversationRepository) {
        return new ListConversationsUseCase(conversationRepository);
    }

    @Bean
    public GetMessagesByConversationPort getMessagesByConversationPort(MessageRepositoryPort messageRepository) {
        return new GetMessagesByConversationUseCase(messageRepository);
    }

    @Bean("sendMessageUseCase")
    public SendMessagePort sendMessageUseCase(
            ConversationRepositoryPort conversationRepository,
            MessageRepositoryPort messageRepository,
            TelegramPort telegramPort) {
        return new SendMessageUseCase(conversationRepository, messageRepository, telegramPort);
    }

    @Bean
    @Primary
    public SendMessagePort transactionalSendMessageUseCase(
            @Qualifier("sendMessageUseCase") SendMessagePort useCase) {
        return new TransactionalSendMessageUseCase(useCase);
    }
}
