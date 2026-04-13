package com.aegis.autochess.exception;

public class InsufficientGoldException extends RuntimeException {
    public InsufficientGoldException(String message) {
        super(message);
    }
}