package de.htwsaar.vs.gruppe05.client.controllers;

public class LoginResponse {
    private String message;

    public LoginResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
