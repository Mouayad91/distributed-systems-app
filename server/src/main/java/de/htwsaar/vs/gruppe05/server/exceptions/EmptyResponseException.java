package de.htwsaar.vs.gruppe05.server.exceptions;


/**
 * EmptyResponseException is thrown if no matching Resource found
 */
public class EmptyResponseException extends RuntimeException {

    public EmptyResponseException(String message) {
        super(message);
    }

    public EmptyResponseException(String message, Throwable cause) {
        super(message, cause);
    }
}
