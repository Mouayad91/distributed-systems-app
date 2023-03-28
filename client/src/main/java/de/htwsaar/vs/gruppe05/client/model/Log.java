package de.htwsaar.vs.gruppe05.client.model;

import java.util.List;

public class Log {
    private List<LogEntry> logEntries;
    private long currentPage;
    private long totalPage;
    private long elementCounter;
    private boolean hasNext;


    public Log(List<LogEntry> logs, long currentPage, long totalPage, long elementCounter, boolean hasNext) {
        this.logEntries = logs;
        this.currentPage = currentPage;
        this.totalPage = totalPage;
        this.elementCounter = elementCounter;
        this.hasNext = hasNext;
    }


    public List<LogEntry> getLogEntries() {
        return logEntries;
    }

    public long getCurrentPage() {
        return currentPage;
    }

    public long getElementCounter() {
        return elementCounter;
    }

    public long getTotalPage() {
        return totalPage;
    }


    public void setCurrentPage(long currentPage) {
        this.currentPage = currentPage;
    }

    public void setElementCounter(long elementCounter) {
        this.elementCounter = elementCounter;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public void setLogEntries(List<LogEntry> logEntries) {
        this.logEntries = logEntries;
    }

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }
}
