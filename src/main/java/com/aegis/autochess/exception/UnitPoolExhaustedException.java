package com.aegis.autochess.exception;

public class UnitPoolExhaustedException extends RuntimeException {
    public UnitPoolExhaustedException(String message) {
        super(message);
    }
}