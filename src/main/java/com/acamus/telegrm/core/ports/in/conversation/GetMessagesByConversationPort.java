package com.acamus.telegrm.core.ports.in.conversation;

import com.acamus.telegrm.core.domain.model.Message;
import java.util.List;

public interface GetMessagesByConversationPort {
    List<Message> getMessages(String conversationId);
}
