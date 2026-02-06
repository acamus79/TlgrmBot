package com.acamus.telegrm.core.ports.in.conversation;

public record SendMessageCommand(String conversationId, String content) {}
