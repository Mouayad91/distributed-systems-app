package de.htwsaar.vs.gruppe05.server.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.htwsaar.vs.gruppe05.server.enums.StatusEnums;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Invitation Model Class
 *
 * @version 20.02.2023
 */
@Entity
@Table(name = "invitations")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) //ignore hibernate initializer at serialization
@NoArgsConstructor
public class Invitation {


    @Id
    @Column(name = "invitationId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "Invitation needs status")
    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private StatusEnums.InvitationStatus status;


    @NotNull(message = "Invitation needs related User")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;


    @NotNull(message = "Invitation needs related Appointment")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointmentId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Appointment appointment;

    @PastOrPresent(message = "Creation Date has to be in the Past")
    @Column(name = "creationDate", updatable = false, columnDefinition = "datetime(0)")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime creationDate;

    public Invitation(int status, int userId, int appointmentId) {}

    @PrePersist
    private void onCreate() {
        creationDate = LocalDateTime.now().withNano(0);
    }

}
