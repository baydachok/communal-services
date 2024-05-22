package ru.baydak.dto;

/**
 * A record representing a response containing an authentication token.
 * This record encapsulates the authentication token.
 */
public record TokenResponse
        (
                String token
        ) {
}
