package com.microdev.automation.restng.exceptions;

/**
 * Created by wuchao on 17/7/24.
 */
public class RestNgDBException extends RuntimeException {

    public RestNgDBException() {
    }

    public RestNgDBException(String message) {
        super(message);
    }

    public RestNgDBException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestNgDBException(Throwable cause) {
        super(cause);
    }

    public RestNgDBException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
