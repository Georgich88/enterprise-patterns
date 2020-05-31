package com.isaev.ee.lazyload.database.exception;

public class DataMapperException extends Exception {

    public DataMapperException() {
        super();
    }

    public DataMapperException(String message) {
        super(message);
    }

    public DataMapperException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataMapperException(Throwable cause) {
        super(cause);
    }
}

