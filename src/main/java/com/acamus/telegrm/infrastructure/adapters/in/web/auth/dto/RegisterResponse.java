package com.acamus.telegrm.infrastructure.adapters.in.web.auth.dto;

public record RegisterResponse(
    String id,
    String email,
    String name
) {}
