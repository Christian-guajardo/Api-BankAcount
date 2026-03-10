package com.example.clientAPI.exception;

public class FunctionalException extends RuntimeException {

    private final String code;

    public FunctionalException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
