package de.htwsaar.vs.gruppe05.server.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.htwsaar.vs.gruppe05.server.enums.StatusEnums;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * UserDto capsules User
 * @version 20.02.2023
 */
@Data
public class UserDto {
    private long id;
    private String userName;

    private String firstName;

    private String lastName;
    private String email;
    private String password;
    private LocalDateTime creationDate;
    private LocalDateTime lastLoginDate;
    private boolean enabled;
    private StatusEnums.Role role;

    public UserDto() {}

    public UserDto(long id, String userName, String firstName, String lastName, String email, String password, LocalDateTime creationDate, LocalDateTime lastLoginDate, boolean enabled, StatusEnums.Role role) {
        this.id = id;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.creationDate = creationDate;
        this.lastLoginDate = lastLoginDate;
        this.enabled = enabled;
        this.role = role;
    }
}
