package com.acamus.telegrm.core.ports.in.user;

public interface AuthenticateUserPort {
    String authenticate(AuthenticateUserCommand command);
}
