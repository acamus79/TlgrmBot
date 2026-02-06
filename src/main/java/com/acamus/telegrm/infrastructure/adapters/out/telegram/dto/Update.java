package com.acamus.telegrm.infrastructure.adapters.out.telegram.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Update(
    @JsonProperty("update_id") long updateId,
    MessageDto message
) {}
