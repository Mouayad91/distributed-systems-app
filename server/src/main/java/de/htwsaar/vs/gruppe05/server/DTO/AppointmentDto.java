package de.htwsaar.vs.gruppe05.server.DTO;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * AppointmentDto capsules Appointment
 * @version 20.02.2023
 */
@Data
public class AppointmentDto {
    private long id;
    private String title;
    private String description;
    private LocalDateTime creationDate; //set in service class
    private LocalDateTime lastEditDate;
    private long userId;
    private String location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

}
