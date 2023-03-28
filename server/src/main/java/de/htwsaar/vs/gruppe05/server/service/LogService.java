package de.htwsaar.vs.gruppe05.server.service;

import de.htwsaar.vs.gruppe05.server.enums.LogType;
import de.htwsaar.vs.gruppe05.server.exceptions.EmptyResponseException;
import de.htwsaar.vs.gruppe05.server.model.LogEntry;
import de.htwsaar.vs.gruppe05.server.repository.LogRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Logging Service
 *
 * @version 20.02.2023
 */
@Service
public class LogService {
    @Autowired
    private LogRepository logRepository;

    private LogEntry createUserLog(LogEntry logEntry) {
        return logRepository.save(logEntry);
    }

    public LogEntry createUserLog(String msg) {
        LogEntry logEntry = new LogEntry(LogType.USER, msg);
        return createUserLog(logEntry);
    }

    public LogEntry createAppointmentLog(String msg) {
        LogEntry logEntry = new LogEntry(LogType.APPOINTMENT, msg);
        return createUserLog(logEntry);
    }

    public LogEntry createInvitationLog(String msg) {
        LogEntry logEntry = new LogEntry(LogType.INVITATION, msg);
        return createUserLog(logEntry);
    }

    public LogEntry createLoggingLog(String msg) {
        LogEntry logEntry = new LogEntry(LogType.LOG, msg);
        return createUserLog(logEntry);
    }

    public LogEntry getLogById(Long id) {

        Optional<LogEntry> entry = logRepository.findById(id);
        if(entry.isEmpty()){
            throw new EntityNotFoundException("No LogEntry found with {" + id + "}");
        }

        return entry.get();
    }

    public Page<LogEntry> getLogsPaged(Pageable pageable) {
        return logRepository.findAll(pageable);
    }

    public List<LogEntry> getLogEntriesByType(LogType logType) {
        return logRepository.findAllByLogType(logType).orElseThrow(() -> new EmptyResponseException("No User Entry found in User-Table"));
    }
}
