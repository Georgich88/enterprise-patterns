package com.isaev.ee.activerecord.database.exception;

public class ActiveRecordException extends Exception {

    public ActiveRecordException() {
        super();
    }

    public ActiveRecordException(String message) {
        super(message);
    }

    public ActiveRecordException(String message, Throwable cause) {
        super(message, cause);
    }

    public ActiveRecordException(Throwable cause) {
        super(cause);
    }
}

