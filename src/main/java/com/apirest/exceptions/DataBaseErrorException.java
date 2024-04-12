package com.apirest.exceptions;

public class DataBaseErrorException extends RuntimeException {
    public DataBaseErrorException(String message) {
        super(message);
    }

    public DataBaseErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
