package com.isaev.ee.tabledatagateway.database.exception;

public class DataGatewayException extends Exception {

    public DataGatewayException() {
        super();
    }

    public DataGatewayException(String message) {
        super(message);
    }

    public DataGatewayException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataGatewayException(Throwable cause) {
        super(cause);
    }
}

