package de.htwsaar.vs.gruppe05.server.exceptions;


/**
 * API Error Wrapper - Should be returned if object is invalid
 */
public class ApiValidationError implements ApiSubErrors {
    private final String object;
    private String field;
    private Object rejectedValue;
    private final String message;

    public ApiValidationError(String object, String message) {
        this.object = object;
        this.message = message;
    }

    public ApiValidationError(String object, String field, Object rejectedValue, String message) {
        this.object = object;
        this.field = field;
        this.rejectedValue = rejectedValue;
        this.message = message;
    }
}
