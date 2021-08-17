package com.zpi.domain.token.ticketRequest.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RequestErrorType {
    UNAUTHORIZED_CLIENT("UNAUTHORIZED_CLIENT"),
    INVALID_REQUEST("INVALID_REQUEST"),
    UNSUPPORTED_RESPONSE_TYPE("UNSUPPORTED_RESPONSE_TYPE"),
    INVALID_SCOPE("INVALID_SCOPE"),
    UNRECOGNIZED_REDIRECT_URI("UNRECOGNIZED_REDIRECT_URI"),
    USER_AUTH_FAILED("USER_AUTH_FAILED");

    private final String name;
}