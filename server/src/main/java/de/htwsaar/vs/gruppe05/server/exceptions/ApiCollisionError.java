package de.htwsaar.vs.gruppe05.server.exceptions;

/**
 * API Error Wrapper - Should be returned if an Appointment collides
 */
public class ApiCollisionError implements ApiSubErrors{
    private long appointmentId;

    private long collidedAppointmentId;

    private long affectedUserId;

    private String message;

    public ApiCollisionError(long appointmentId, long collisionAppointmentId, String message, long affectedUserId) {
        this.appointmentId = appointmentId;
        this.collidedAppointmentId = collisionAppointmentId;
        this.message = message;
        this.affectedUserId = affectedUserId;
    }

}
