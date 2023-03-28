package de.htwsaar.vs.gruppe05.server.rest;

import de.htwsaar.vs.gruppe05.server.DTO.LogDto;
import de.htwsaar.vs.gruppe05.server.enums.LogType;
import de.htwsaar.vs.gruppe05.server.mapper.LogPageMapper;
import de.htwsaar.vs.gruppe05.server.model.LogEntry;
import de.htwsaar.vs.gruppe05.server.service.LogService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * LogController
 *
 * @version 20.02.2023
 */
@RestController
@RequestMapping("/api/logs")
//@SecurityRequirement(name = "Bearer Authentication")
public class LogController {
    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    /**
     * Get log by id
     * @param id the logId
     * @return the logEntry object
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<LogEntry> getLogById(@PathVariable("id") Long id) {
        logService.createLoggingLog("Requested log by id");
        return new ResponseEntity<>(logService.getLogById(id), HttpStatus.OK);
    }

    /**
     * Get parsed logs dto
     * @param page the page
     * @param size the size
     * @return the LogDto
     */
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<LogDto> getLogsPaged(@RequestParam int page, @RequestParam int size) {
    	logService.createLoggingLog("Get parsed logs");
        Pageable paging = PageRequest.of(page, size);
        Page<LogEntry> logEntries = logService.getLogsPaged(paging);
        return new ResponseEntity<>(LogPageMapper.toDto(logEntries), HttpStatus.OK);
    }

    /**
     * Get logs by logs type
     * @param logType the log type
     * @return the list of logs with specific types
     */
    @GetMapping("/{mode}/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<LogEntry>> getLogsByType(@PathVariable LogType logType) {
    	logService.createLoggingLog("Took logs by type");
        return new ResponseEntity<>(logService.getLogEntriesByType(logType), HttpStatus.OK);
    }

}
