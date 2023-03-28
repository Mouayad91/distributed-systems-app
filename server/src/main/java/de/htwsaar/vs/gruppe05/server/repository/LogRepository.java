package de.htwsaar.vs.gruppe05.server.repository;

import de.htwsaar.vs.gruppe05.server.enums.LogType;
import de.htwsaar.vs.gruppe05.server.model.LogEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LogRepository extends JpaRepository<LogEntry, Long> {
    @Override
    List<LogEntry> findAll();

    Optional<List<LogEntry>> findAllByLogType(LogType logType);

    @Override
    Page<LogEntry> findAll(Pageable pageable);

    Optional<LogEntry> findById(Long id);

}
