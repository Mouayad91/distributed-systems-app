package de.htwsaar.vs.gruppe05.client.model;

import de.htwsaar.vs.gruppe05.client.service.Codes;

public class ApiResult<T> {

    private Codes.StatusCode code;

    private String message;

    private T responseContent;

    public ApiResult(Codes.StatusCode code, String message) {
        this.message = message;
        this.code = code;
    }

    public ApiResult(Codes.StatusCode code, String message, T responseContent) {
        this.code = code;
        this.message = message;
        this.responseContent = responseContent;
    }

    public T getResponseContent() {
        return responseContent;
    }

    public Codes.StatusCode getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
