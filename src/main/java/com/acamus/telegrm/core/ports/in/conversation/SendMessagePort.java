package com.acamus.telegrm.core.ports.in.conversation;

public interface SendMessagePort {
    void sendMessage(SendMessageCommand command);
}
