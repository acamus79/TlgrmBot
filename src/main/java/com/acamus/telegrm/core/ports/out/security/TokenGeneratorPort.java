package com.acamus.telegrm.core.ports.out.security;

import com.acamus.telegrm.core.domain.model.User;

public interface TokenGeneratorPort {
    String generateToken(User user);
}
