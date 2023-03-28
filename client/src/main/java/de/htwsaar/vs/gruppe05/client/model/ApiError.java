package de.htwsaar.vs.gruppe05.client.model;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ApiError {


    private HttpStatus httpStatus;
    private String message;
    private String description;

    private LocalDateTime timestamp;

    public ApiError() {

    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
