package de.htwsaar.vs.gruppe05.client.controllers;

import de.htwsaar.vs.gruppe05.client.model.Log;
import de.htwsaar.vs.gruppe05.client.model.LogEntry;
import de.htwsaar.vs.gruppe05.client.routing.AppRouter;
import de.htwsaar.vs.gruppe05.client.service.ApiLogService;
import de.htwsaar.vs.gruppe05.client.service.Codes;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

@Component
public class AdminLogView {

    @FXML
    private TableView<LogEntry> logTable;

    @FXML
    private TableColumn<LogEntry, Long> id;

    @FXML
    private TableColumn<LogEntry, Codes.LogType> logType;

    @FXML
    private TableColumn<LogEntry, String> description;

    @FXML
    private TableColumn<LogEntry, LocalDateTime> timestamp;

    @FXML
    private Pagination paginationfx;

    @Autowired
    private AppRouter appRouter;

    @Autowired
    private ApiLogService apiLogService;


    private final int pageSize = 10;
    private final int size = 10;

    @FXML
    public void initialize() throws URISyntaxException, IOException, InterruptedException {
        // Set up the table columns
        id.setCellValueFactory(new PropertyValueFactory<LogEntry, Long>("id"));
        logType.setCellValueFactory(new PropertyValueFactory<LogEntry, Codes.LogType>("logType"));
        description.setCellValueFactory(new PropertyValueFactory<LogEntry, String>("description"));
        timestamp.setCellValueFactory(new PropertyValueFactory<LogEntry, LocalDateTime>("timestamp"));
        paginationfx.setPageFactory(this::createPage);
        //try{
        //    logTable.getItems().setAll(this.apiLogService.fetchAllLogs(1,4).getLogEntries());
        //}catch (IOException ex){
        //    System.out.println(ex);
        //} catch (URISyntaxException | InterruptedException e) {
        //   throw new RuntimeException(e);
        //}
    }


    private Node createPage(int pageIndex) {

        int fromIndex = pageIndex * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, size);
        try {
            Log logs = this.apiLogService.fetchAllLogs(fromIndex, toIndex);
            logTable.setItems(FXCollections.observableArrayList(logs.getLogEntries()));
            paginationfx.setPageCount((int) logs.getTotalPage() / pageSize + 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return logTable;
    }


    public void goBack() throws IOException {
        appRouter.popRoute();
    }

}
