package com.wandercosta.pokergame.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponse {

    public ErrorResponse(final HttpStatus status, final String message) {
        this.code = status.value();
        this.status = status.getReasonPhrase();
        this.message = message;
    }

    private final int code;
    private final String status;
    private final String message;
}
