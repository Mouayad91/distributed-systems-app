package de.htwsaar.vs.gruppe05.server.exceptions;

import de.htwsaar.vs.gruppe05.server.model.Appointment;

/**
 * Appointment Collision Exception - Carries Information about Collision
 */
public class AppointmentCollisionException extends RuntimeException {

    public Appointment appointment;

    public Appointment collidedAppointment;

    public AppointmentCollisionException(String message) {
        super(message);
    }

    public AppointmentCollisionException(String message, Appointment appointment, Appointment collidedAppointment) {
        super(message);
        this.appointment = appointment;
        this.collidedAppointment = collidedAppointment;
    }

    public AppointmentCollisionException(String message, Throwable cause) {
        super(message, cause);
    }
}
