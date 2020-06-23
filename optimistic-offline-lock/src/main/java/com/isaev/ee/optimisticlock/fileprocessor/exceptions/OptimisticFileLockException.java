package com.isaev.ee.optimisticlock.fileprocessor.exceptions;

public class OptimisticFileLockException extends Exception {

    public OptimisticFileLockException() {
        super();
    }

    public OptimisticFileLockException(String message) {
        super(message);
    }

    public OptimisticFileLockException(String message, Throwable cause) {
        super(message, cause);
    }

    public OptimisticFileLockException(Throwable cause) {
        super(cause);
    }

    protected OptimisticFileLockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
