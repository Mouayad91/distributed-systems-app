package de.htwsaar.vs.gruppe05.server.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

/**
 * Appointments Model Class
 *
 * @version 20.02.2023
 **/
@Entity
@Table(name = "appointments")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) //ignore hibernate initializer at serialization
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Appointment title is mandatory")
    @Column(name = "title", updatable = true, nullable = false)
    private String title;


    @Column(name = "description")
    private String description;


    @PastOrPresent(message = "Creation Date has to be in the Past/Present")
    @Column(name = "creationDate", updatable = false, nullable = false)
    private LocalDateTime creationDate;

    @PastOrPresent(message = "Last Edit Date has to be in the Past/Present")
    @Column(name = "lastEditDate", updatable = true, nullable = false)
    private LocalDateTime lastEditDate;

    @NotNull(message = "Appointment needs related Creator/User")
    @JoinColumn(name = "userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User creator;


    @NotBlank(message = "Appointment location is mandatory")
    @Column(name = "location")
    private String location;


    @FutureOrPresent(message = "Start Time has to be in the Present/Future")
    @Column(name = "startTime")
    private LocalDateTime startTime;


    @FutureOrPresent(message = "End Time has to be in the Present/Future")
    @Column(name = "endTime")
    private LocalDateTime endTime;

    public Appointment(String title, String description, User creator, String location, LocalDateTime startTime, LocalDateTime endTime) {
        this.title = title;
        this.description = description;
        this.creator = creator;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        // this.keywords = keywords;
    }

    @PrePersist
    protected void onCreate() {
        creationDate = LocalDateTime.now().withNano(0);
        lastEditDate = LocalDateTime.now().withNano(0);
    }

    @PreUpdate
    protected void onUpdate() {
        lastEditDate = LocalDateTime.now().withNano(0);
    }

    public boolean checkCollision(Appointment appointment){
        return !(endTime.isBefore(appointment.getStartTime()) || appointment.getEndTime().isBefore(startTime));
    }

}
