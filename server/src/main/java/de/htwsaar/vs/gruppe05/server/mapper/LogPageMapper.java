package de.htwsaar.vs.gruppe05.server.mapper;

import de.htwsaar.vs.gruppe05.server.DTO.LogDto;
import de.htwsaar.vs.gruppe05.server.model.LogEntry;
import org.springframework.data.domain.Page;

public class LogPageMapper {
    public static LogDto toDto(Page<LogEntry> page){
        LogDto logDto = new LogDto();
        logDto.setLogEntries(page.toList());
        logDto.setCurrentPage(page.getNumber());
        logDto.setTotalPage(page.getTotalPages());
        logDto.setElementCounter(page.getTotalElements());
        logDto.setHasNext(page.hasNext());
        return logDto;
    }
}
