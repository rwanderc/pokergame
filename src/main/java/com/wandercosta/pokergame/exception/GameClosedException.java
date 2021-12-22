package com.wandercosta.pokergame.exception;

public class GameClosedException extends IllegalStateException {

    public GameClosedException(final String message) {
        super(message);
    }
}
