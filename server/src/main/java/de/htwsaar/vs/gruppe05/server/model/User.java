package de.htwsaar.vs.gruppe05.server.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.htwsaar.vs.gruppe05.server.enums.StatusEnums;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


/**
 * Basic Customer Model Class
 *
 * @version v.0.1 - 26.01.2023
 */

@Entity
@Table(name = "user")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) //ignore hibernate initializer at serialization
@AllArgsConstructor
@NoArgsConstructor
public class User {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "userId")
    private long id;

    @NotBlank(message = "User is mandatory")
    @Column(name = "userName", unique = true)
    private String userName;

    @Email(message = "Invalid E-Mail")
    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @NotBlank(message = "Password is mandatory")
    @Column(name = "password")
    private String password;

    @PastOrPresent(message = "Creation Date has to be in the Past")
    @Column(name = "creationDate", updatable = false, columnDefinition = "datetime(0)")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime creationDate;

    @PastOrPresent(message = "Last Login Date has to be in the Past")
    @Column(name = "lastLoginDate", updatable = true, columnDefinition = "datetime(0)")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime lastLoginDate;

    @Column(name = "enabled")
    private boolean enabled;

    @NotNull(message = "User needs Role")
    @Column(name = "role")
    @Enumerated(EnumType.ORDINAL)
    private StatusEnums.Role role;

    public User(String userName, String email, String firstName, String lastName) {
        this.userName = userName;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @PrePersist
    private void onCreate() {
        creationDate = LocalDateTime.now().withNano(0);
    }

}
