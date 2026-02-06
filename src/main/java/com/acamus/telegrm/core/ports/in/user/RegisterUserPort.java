package com.acamus.telegrm.core.ports.in.user;

import com.acamus.telegrm.core.domain.model.User;

public interface RegisterUserPort {
    User register(RegisterUserCommand command);
}
