package com.isaev.ee.transactionscript.database.exceptions;

@SuppressWarnings("serial")
public class NoDatabasePropertiesFileException extends RuntimeException {

    public NoDatabasePropertiesFileException(String message, Throwable cause) {
        super(message, cause);
    }


}
