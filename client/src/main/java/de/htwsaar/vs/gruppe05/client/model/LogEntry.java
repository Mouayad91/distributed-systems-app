package de.htwsaar.vs.gruppe05.client.model;

import de.htwsaar.vs.gruppe05.client.service.Codes;

import java.time.LocalDateTime;

public class LogEntry {

    private Long id;

    private Codes.LogType logType;

    private String description;
    private LocalDateTime timestamp;

    public LogEntry(Codes.LogType logType, String description, LocalDateTime timestamp) {
        this.logType = logType;
        this.description = description;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Codes.LogType getLogType() {
        return logType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLogType(Codes.LogType logType) {
        this.logType = logType;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
