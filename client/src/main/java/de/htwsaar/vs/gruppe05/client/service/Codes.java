package de.htwsaar.vs.gruppe05.client.service;

public class Codes {

    public enum StatusCode {
        SUCCESS,
        FAILURE,
        UNKNOWN,
        UNAUTHORIZED,
        CONFLICT,
    }

    public enum LogType {
        INVITATION,
        APPOINTMENT,
        USER,
        LOG
    }
}
