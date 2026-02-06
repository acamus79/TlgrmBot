package com.acamus.telegrm.infrastructure.adapters.out.persistence;

import com.acamus.telegrm.BaseIntegrationTest;
import com.acamus.telegrm.core.domain.model.Conversation;
import com.acamus.telegrm.core.domain.valueobjects.TelegramChatId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ConversationRepositoryAdapterTest extends BaseIntegrationTest {

    @Autowired
    private ConversationRepositoryAdapter adapter;

    @Test
    @DisplayName("GIVEN a conversation WHEN saved and retrieved by chat ID THEN data is consistent")
    void saveAndFindByTelegramChatId_shouldReturnConsistentData() {
        // GIVEN
        long chatId = 98765L;
        Conversation newConversation = Conversation.create(
                new TelegramChatId(chatId),
                "Jane",
                "Doe",
                "janedoe"
        );

        // WHEN
        adapter.save(newConversation);
        Optional<Conversation> foundConversationOpt = adapter.findByTelegramChatId(chatId);

        // THEN
        assertThat(foundConversationOpt).isPresent();
        Conversation foundConversation = foundConversationOpt.get();
        assertThat(foundConversation.getId()).isEqualTo(newConversation.getId());
        assertThat(foundConversation.getTelegramChatId().value()).isEqualTo(chatId);
        assertThat(foundConversation.getFirstName()).isEqualTo("Jane");
    }

    @Test
    @DisplayName("GIVEN a saved conversation WHEN findById is called THEN it returns the conversation")
    void findById_shouldReturnConversation() {
        // GIVEN
        Conversation conversation = Conversation.create(new TelegramChatId(111L), "Alice", "Smith", "alice");
        adapter.save(conversation);

        // WHEN
        Optional<Conversation> found = adapter.findById(conversation.getId());

        // THEN
        assertThat(found).isPresent();
        assertThat(found.get().getTelegramChatId().value()).isEqualTo(111L);
    }

    @Test
    @DisplayName("GIVEN multiple saved conversations WHEN findAll is called THEN it returns all of them")
    void findAll_shouldReturnAllConversations() {
        // GIVEN
        adapter.save(Conversation.create(new TelegramChatId(222L), "Bob", "Jones", "bob"));
        adapter.save(Conversation.create(new TelegramChatId(333L), "Charlie", "Brown", "charlie"));

        // WHEN
        var allConversations = adapter.findAll();

        // THEN
        assertThat(allConversations).hasSizeGreaterThanOrEqualTo(2);
    }
}
