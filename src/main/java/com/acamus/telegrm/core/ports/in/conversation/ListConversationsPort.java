package com.acamus.telegrm.core.ports.in.conversation;

import com.acamus.telegrm.core.domain.model.Conversation;
import java.util.List;

public interface ListConversationsPort {
    List<Conversation> listAll();
}
