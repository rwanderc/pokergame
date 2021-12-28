package com.wandercosta.pokergame.exception;

public class EntityNotFoundException extends IllegalArgumentException {

    public EntityNotFoundException(final String message) {
        super(message);
    }

    public EntityNotFoundException() {
        super("Entity not found");
    }
}
