package de.htwsaar.vs.gruppe05.server.model;


import de.htwsaar.vs.gruppe05.server.enums.LogType;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Log Entry Model Class
 *
 * @version 20.02.2023
 */
@Entity
@Table(name = "logs")
public class LogEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private LogType logType;

    @Column(name = "description")
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    public LogEntry() {
    }

    public LogEntry(LogType logType, String description) {
        this.logType = logType;
        this.description = description;
        this.timestamp = LocalDateTime.now().withNano(0);
    }

    // getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LogType getLogType() {
        return logType;
    }

    public void setLogType(LogType logType) {
        this.logType = logType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
