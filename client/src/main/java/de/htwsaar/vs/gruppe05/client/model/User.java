package de.htwsaar.vs.gruppe05.client.model;

import de.htwsaar.vs.gruppe05.client.model.enums.RoleEnums;

import java.time.LocalDateTime;

public class User {
    private long id = 0;
    private String userName = "";
    private String email = "";
    private String password = "";
    private LocalDateTime creationDate;
    private LocalDateTime lastLoginDate;
    private boolean enabled;
    private RoleEnums.Role role;

    private String firstName;

    private String lastName;


    public User(String userName) {
        this.userName = userName;
    }


    public long getId() {
        return id;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getLastLoginDate() {
        return lastLoginDate;
    }

    public RoleEnums.Role getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setLastLoginDate(LocalDateTime lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(RoleEnums.Role role) {
        this.role = role;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}
