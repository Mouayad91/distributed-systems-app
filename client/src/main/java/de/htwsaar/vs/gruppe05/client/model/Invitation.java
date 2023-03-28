package de.htwsaar.vs.gruppe05.client.model;

import de.htwsaar.vs.gruppe05.client.model.enums.StatusEnums;

import java.time.LocalDateTime;

public class Invitation {
    private long id;
    private StatusEnums.InvitationStatus status = StatusEnums.InvitationStatus.PENDING;
    private long userId;
    private long appointmentId;
    private LocalDateTime creationDate;


    public Invitation(long userId, long appointmentId) {
        this.userId = userId;
        this.appointmentId = appointmentId;
        this.creationDate = LocalDateTime.now();
    }

    public Invitation(long id, StatusEnums.InvitationStatus status, long userId, long appointmentId, LocalDateTime creationDate) {
        this.userId = userId;
        this.appointmentId = appointmentId;
        this.creationDate = creationDate;
        this.id = id;
        this.status = status;
    }


    public Invitation() {
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public StatusEnums.InvitationStatus getStatus() {
        return status;
    }

    public long getAppointmentId() {
        return appointmentId;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setAppointmentId(long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public void setStatus(StatusEnums.InvitationStatus status) {
        this.status = status;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
