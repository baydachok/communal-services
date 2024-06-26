package ru.baydak.service.impl;

import ru.baydak.auditstarter.annotation.Auditable;
import ru.baydak.dto.TokenResponse;
import ru.baydak.exception.InvalidCredentialsException;
import ru.baydak.exception.NotValidArgumentException;
import ru.baydak.exception.SecurityException;
import ru.baydak.loggingstarter.annotations.LoggableInfo;
import ru.baydak.loggingstarter.annotations.LoggableTime;
import ru.baydak.model.entity.User;
import ru.baydak.model.types.Role;
import ru.baydak.security.JwtTokenUtils;
import ru.baydak.service.SecurityService;
import ru.baydak.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link SecurityService} interface.
 */
@Service
@LoggableTime
@LoggableInfo(name = "class - SecurityServiceImpl")
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtils jwtTokenUtils;

    /**
     * Registers a new user with the provided login and password.
     *
     * @param login    the user's login
     * @param password the user's password
     * @return the registered user
     * @throws NotValidArgumentException if login or password is empty, blank, or does not meet length requirements
     * @throws SecurityException         if a user with the same login already exists
     */
    @Override
    @Auditable
    public User register(String login, String password) {
        if (login == null || password == null || login.isEmpty() || password.isEmpty() || login.isBlank() || password.isBlank()) {
            throw new NotValidArgumentException("Пароль или логин не могут быть пустыми или состоять только из пробелов.");
        }

        if (password.length() < 5 || password.length() > 30) {
            throw new NotValidArgumentException("Длина пароля должна составлять от 5 до 30 символов.");
        }

        if (userService.getUserByLogin(login).isPresent()) {
            throw new SecurityException("User with this login already exists");
        }

        User newUser = User.builder()
                .login(login)
                .role(Role.USER)
                .password(passwordEncoder.encode(password))
                .build();

        return userService.save(newUser);
    }

    /**
     * Authorizes a user with the provided login and password.
     *
     * @param login    the user's login
     * @param password the user's password
     * @return an optional containing the authorized user, or empty if authorization fails
     * @throws SecurityException if the user is not found or the password is incorrect
     */
    @Override
    @Auditable
    public TokenResponse authorize(String login, String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login, password)
            );
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(login);
        String token = jwtTokenUtils.generateToken(userDetails);

        return new TokenResponse(token);
    }
}
