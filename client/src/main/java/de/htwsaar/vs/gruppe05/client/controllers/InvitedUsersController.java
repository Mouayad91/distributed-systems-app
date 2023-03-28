package de.htwsaar.vs.gruppe05.client.controllers;

import de.htwsaar.vs.gruppe05.client.model.Appointment;
import de.htwsaar.vs.gruppe05.client.model.Invitation;
import de.htwsaar.vs.gruppe05.client.model.enums.StatusEnums;
import de.htwsaar.vs.gruppe05.client.service.ApiAppointmentService;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

@Component
public class InvitedUsersController {
    @FXML
    private TableView<Invitation> invitationTableView;

    @FXML
    private TableColumn<Invitation, Long> userId;

    @FXML
    private TableColumn<Invitation, Long> invitationId;

    @FXML
    private TableColumn<Invitation, LocalDateTime> creationDate;

    @FXML
    private TableColumn<Invitation, StatusEnums.InvitationStatus> status;

    @FXML
    private TableColumn<Invitation, Long> id;

    @Autowired
    private ApiAppointmentService apiAppointmentService;

    private Appointment appointment;

    @FXML
    public void initialize() throws URISyntaxException, IOException, InterruptedException {
        invitationTableView.getItems().clear();

        // Set up the table columns
        id.setCellValueFactory(new PropertyValueFactory<Invitation, Long>("Id"));
        userId.setCellValueFactory(new PropertyValueFactory<Invitation, Long>("UserId"));
        invitationId.setCellValueFactory(new PropertyValueFactory<Invitation, Long>("appointmentId"));
        creationDate.setCellValueFactory(new PropertyValueFactory<Invitation, LocalDateTime>("creationDate"));
        status.setCellValueFactory(new PropertyValueFactory<Invitation, StatusEnums.InvitationStatus>("Status"));

    }

    public void initInvitations(Appointment appointment) {
        this.appointment = appointment;
        try {
            invitationTableView.getItems().setAll(this.apiAppointmentService.getAllInvitationsToAppointment(appointment.getId()));
        } catch (IOException ex) {
            System.out.println(ex);
        } catch (URISyntaxException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
