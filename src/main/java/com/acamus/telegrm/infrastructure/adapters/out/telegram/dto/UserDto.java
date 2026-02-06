package com.acamus.telegrm.infrastructure.adapters.out.telegram.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserDto(
        long id,
        @JsonProperty("is_bot") boolean isBot,
        @JsonProperty("first_name") String firstName,
        @JsonProperty("last_name") String lastName,
        String username
) {}