package com.pw.mwo.exceptions;

public class EntityAlreadyExistsException extends RuntimeException {
    public EntityAlreadyExistsException() {
        super("Entity already exists");
    }
}
