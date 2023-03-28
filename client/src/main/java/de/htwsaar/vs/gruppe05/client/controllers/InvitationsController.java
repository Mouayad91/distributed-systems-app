package de.htwsaar.vs.gruppe05.client.controllers;

import de.htwsaar.vs.gruppe05.client.model.Invitation;
import de.htwsaar.vs.gruppe05.client.model.enums.StatusEnums;
import de.htwsaar.vs.gruppe05.client.routing.AppRouter;
import de.htwsaar.vs.gruppe05.client.service.ApiInvitationService;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class InvitationsController {
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
    private ApiInvitationService apiInvitationService;

    @Autowired
    private AppRouter appRouter;


    @FXML
    public void initialize() throws URISyntaxException, IOException, InterruptedException {
        invitationTableView.getItems().clear();

        // Set up the table columns
        id.setCellValueFactory(new PropertyValueFactory<Invitation, Long>("Id"));
        userId.setCellValueFactory(new PropertyValueFactory<Invitation, Long>("UserId"));
        invitationId.setCellValueFactory(new PropertyValueFactory<Invitation, Long>("appointmentId"));
        creationDate.setCellValueFactory(new PropertyValueFactory<Invitation, LocalDateTime>("creationDate"));
        status.setCellValueFactory(new PropertyValueFactory<Invitation, StatusEnums.InvitationStatus>("Status"));
        try {
            invitationTableView.getItems().setAll(this.apiInvitationService.getInvitations());
        } catch (IOException ex) {
            System.out.println(ex);
        } catch (URISyntaxException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<ButtonType> acceptInvitations() {
        Invitation selectedInvitation = invitationTableView.getSelectionModel().getSelectedItem();

        if (selectedInvitation == null) {
            ErrorsAndInfos.showError("Error", "No Invitation Selected", "Please select a Invitation from the table.");
        } else {
            selectedInvitation.setStatus(StatusEnums.InvitationStatus.ACCEPTED);
            try {
                apiInvitationService.updateInvitation(selectedInvitation);
            } catch (Exception e) {
                ErrorsAndInfos.showError("Error", "Invitation update", "Invitation could not be updated");
            }
            refresh();
        }
        return ErrorsAndInfos.showInformation("Success", "Appointment accepted " + selectedInvitation.getId());

    }

    public Optional<ButtonType> declineInvitations() {
        Invitation selectedInvitation = invitationTableView.getSelectionModel().getSelectedItem();

        if (selectedInvitation == null) {
            ErrorsAndInfos.showError("Error", "No User Selected", "Please select a user from the table.");
        } else {
            selectedInvitation.setStatus(StatusEnums.InvitationStatus.DECLINED);
            try {
                apiInvitationService.updateInvitation(selectedInvitation);
            } catch (Exception e) {
                ErrorsAndInfos.showError("Error", "Invitation update", "Invitation could not be updated");
            }
            refresh();
        }
        return ErrorsAndInfos.showInformation("Success", "Appointment declined " + selectedInvitation.getId());

    }

    public void refresh() {
        invitationTableView.getItems().clear();
        try {
            invitationTableView.getItems().addAll(apiInvitationService.getInvitations());
        } catch (Exception e) {
            ErrorsAndInfos.showError("Error", "Could not refresh", "Could not refresh table");
        }
        invitationTableView.refresh();
    }

    @FXML
    void back() throws Exception {
        appRouter.popRoute();
    }
}
