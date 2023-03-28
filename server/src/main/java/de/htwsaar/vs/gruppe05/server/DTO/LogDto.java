package de.htwsaar.vs.gruppe05.server.DTO;

import de.htwsaar.vs.gruppe05.server.model.LogEntry;
import lombok.Data;

import java.util.List;

/**
 * LogDto capsules Logs and Pageable
 * @version 20.02.2023
 */
@Data
public class LogDto {
    private List<LogEntry> logEntries;
    private long currentPage;
    private long totalPage;
    private long elementCounter;
    private boolean hasNext;

}
