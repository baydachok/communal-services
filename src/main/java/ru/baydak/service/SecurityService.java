package ru.baydak.service;

import ru.baydak.dto.TokenResponse;
import ru.baydak.model.entity.User;

/**
 * The service interface for security-related functionality.
 */
public interface SecurityService {

    User register(String login, String password);

    TokenResponse authorize(String login, String password);
}
