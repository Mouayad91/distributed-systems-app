package de.htwsaar.vs.gruppe05.client.model;

public class LoginResponse {
    String token;
    String message;


    public LoginResponse(String token, String message) {
        this.token = token;
        this.message = message;
    }


    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

}
