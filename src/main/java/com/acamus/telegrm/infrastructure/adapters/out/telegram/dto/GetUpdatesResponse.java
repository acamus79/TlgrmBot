package com.acamus.telegrm.infrastructure.adapters.out.telegram.dto;

import java.util.List;

public record GetUpdatesResponse(
    boolean ok,
    List<Update> result
) {}
