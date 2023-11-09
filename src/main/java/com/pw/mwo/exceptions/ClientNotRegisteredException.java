package com.pw.mwo.exceptions;

public class ClientNotRegisteredException extends RuntimeException {
    public ClientNotRegisteredException() {
        super("Client is not registered");
    }
}
