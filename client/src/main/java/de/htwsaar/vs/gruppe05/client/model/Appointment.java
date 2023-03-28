package de.htwsaar.vs.gruppe05.client.model;

import de.htwsaar.vs.gruppe05.client.model.enums.StatusEnums;

import java.time.LocalDateTime;

/**
 * Appointment Model according to backends AppointmentDTO
 */

public class Appointment {

    private long id = 0;
    private String title = "";
    private String description = "";
    private LocalDateTime creationDate;
    private LocalDateTime lastEditDate;
    private long userId = 0;
    private String location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private StatusEnums.InvitationStatus status;

    public Appointment(String title, String description, String location, LocalDateTime startTime, LocalDateTime endTime) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
    }


    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public LocalDateTime getLastEditDate() {
        return lastEditDate;
    }

    public long getUserId() {
        return userId;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public String getLocation() {
        return location;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLastEditDate(LocalDateTime lastEditDate) {
        this.lastEditDate = lastEditDate;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public StatusEnums.InvitationStatus getStatus() {
        return status;
    }

    public void setStatus(StatusEnums.InvitationStatus status) {
        this.status = status;
    }
}
