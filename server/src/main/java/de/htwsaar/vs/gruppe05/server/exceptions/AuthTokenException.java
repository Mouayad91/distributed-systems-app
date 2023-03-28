package de.htwsaar.vs.gruppe05.server.exceptions;

import de.htwsaar.vs.gruppe05.server.enums.AuthExceptionType;
import org.springframework.security.core.AuthenticationException;

/**
 * AuthToken Exception is thrown if TokenError occurs
 */
public class AuthTokenException extends AuthenticationException {

    private AuthExceptionType authExceptionType;

    public AuthTokenException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public AuthTokenException(String msg) {
        super(msg);
    }

    public AuthTokenException(String msg, AuthExceptionType authExceptionType) {
        super(msg);
        this.authExceptionType = authExceptionType;
    }

}
