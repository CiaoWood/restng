package com.microdev.automation.restng.exceptions;

/**
 * Created by wuchao on 17/7/24.
 */
public class RestNgException extends RuntimeException {

    public RestNgException() {
    }

    public RestNgException(String message) {
        super(message);
    }

    public RestNgException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestNgException(Throwable cause) {
        super(cause);
    }

    public RestNgException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
