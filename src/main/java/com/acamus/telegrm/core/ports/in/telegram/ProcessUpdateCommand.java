package com.acamus.telegrm.core.ports.in.telegram;

public record ProcessUpdateCommand(
    long chatId,
    String text,
    String firstName,
    String lastName,
    String username
) {}
