package de.htwsaar.vs.gruppe05.client.controllers;

import de.htwsaar.vs.gruppe05.client.ClientApplication;
import de.htwsaar.vs.gruppe05.client.model.Appointment;
import de.htwsaar.vs.gruppe05.client.model.Invitation;
import de.htwsaar.vs.gruppe05.client.routing.AppRouter;
import de.htwsaar.vs.gruppe05.client.service.ApiAppointmentService;
import de.htwsaar.vs.gruppe05.client.service.ApiInvitationService;
import de.htwsaar.vs.gruppe05.client.service.ApiUserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ContextMenuEvent;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class AdminAppointmentController {


    @FXML
    private TableView<Appointment> appointmentTableView;

    @FXML
    private TableColumn<Appointment, String> title;
    @FXML
    private TableColumn<Appointment, Integer> inviter;

    @FXML
    private TableColumn<Appointment, LocalDateTime> creationDate;

    @FXML
    private TableColumn<Appointment, LocalDateTime> startDate;

    @FXML
    private TableColumn<Appointment, LocalDateTime> endDate;

    @FXML
    private TableColumn<Appointment, LocalDateTime> lastEditDate;


    @FXML
    private TableColumn<Appointment, String> description;


    @FXML
    private Button appointmentsButton;


    @FXML
    private TableColumn<Appointment, String> lct;

    @FXML
    private Button logoutBtn;

    @FXML
    private Button createAppointmentBtn;

    @FXML
    private Button createUserBtn;

    @FXML
    private Button declineBtn;

    @FXML
    private Button acceptBtn;

    @FXML
    private Button showAllUsersBtn;

    @Autowired
    private ApiUserService apiUserService;

    @Autowired
    private ApiAppointmentService apiAppointmentService;

    @Autowired
    private AppRouter appRouter;

    @Autowired
    private ApiInvitationService apiInvitationService;


    private List<Appointment> appointments;


    private List<Invitation> invitations;


    @FXML
    public void initialize() throws URISyntaxException, IOException, InterruptedException {
        // Set up the table columns
        title.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
        inviter.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("id"));
        startDate.setCellValueFactory(new PropertyValueFactory<Appointment, LocalDateTime>("startTime"));
        endDate.setCellValueFactory(new PropertyValueFactory<Appointment, LocalDateTime>("endTime"));
        lct.setCellValueFactory(new PropertyValueFactory<Appointment, String>("location"));
        description.setCellValueFactory(new PropertyValueFactory<Appointment, String>("description"));

        initContextMenu();
        loadData();
        /* try{
            appointmentTableView.getItems().setAll(this.apiAppointmentService.getAllAppointments());
        }catch (IOException ex){
            System.out.println(ex);
        } catch (URISyntaxException | InterruptedException e) {
            throw new RuntimeException(e);
        }*/

    }

    public void initContextMenu() {
        ContextMenu cm = new ContextMenu();
        MenuItem menuItemShow = new MenuItem("Show");
        menuItemShow.setOnAction(this::showContextHandler);
        cm.getItems().add(menuItemShow);

        MenuItem menuItemDelete = new MenuItem("Delete");
        menuItemDelete.setOnAction(this::deleteContextHandler);
        cm.getItems().add(menuItemDelete);

        MenuItem menuItemEdit = new MenuItem("Edit");
        menuItemDelete.setOnAction(this::editContextHandler);
        cm.getItems().add(menuItemEdit);

        appointmentTableView.setContextMenu(cm);
        appointmentTableView.setOnContextMenuRequested(this::onContextMenuRequested);

    }

    private void onContextMenuRequested(ContextMenuEvent event) {
        event.consume();
        boolean show = true;
        if (event.getTarget() instanceof TableCell<?, ?> target) {
            show = !target.isEmpty();
        }
        if (show && !appointmentTableView.getSelectionModel().isEmpty()) {
            appointmentTableView.getContextMenu().show((Node) event.getSource(), event.getScreenX(), event.getScreenY());
        } else {
            appointmentTableView.getContextMenu().hide();
        }
    }

    private void deleteContextHandler(ActionEvent actionEvent) {
        Appointment a = appointmentTableView.getSelectionModel().getSelectedItem();
        System.out.println("Call Delete");
        try {
            apiAppointmentService.deleteAppointment(a.getId());
        } catch (Exception e) {
            ErrorsAndInfos.showError("Could not Delete", "Error", e.getLocalizedMessage());
        }
        loadData();
    }

    private void showContextHandler(ActionEvent actionEvent) {
        Appointment a = appointmentTableView.getSelectionModel().getSelectedItem();
        System.out.println("Called Show");
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ShowAppointmentView.fxml"));
        loader.setControllerFactory(ClientApplication.getApplicationContext()::getBean);

        //eigentlich müsste man hier den router verwenden, weiß aber nicht wie :/
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(false);
        //load appointment into next view
        AppointmentViewController appointmentViewController = loader.getController();
        appointmentViewController.initAppointment(a);
        appointmentViewController.disableFields();
        stage.show();
    }

    private void editContextHandler(ActionEvent actionEvent) {
        Appointment a = appointmentTableView.getSelectionModel().getSelectedItem();
        System.out.println("Called edit");
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ShowAppointmentView.fxml"));
        loader.setControllerFactory(ClientApplication.getApplicationContext()::getBean);

        //eigentlich müsste man hier den router verwenden, weiß aber nicht wie :/
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(false);
        //load appointment into next view
        AppointmentViewController appointmentViewController = loader.getController();
        appointmentViewController.initAppointment(a);
        stage.show();
    }

    public void loadData() {
        appointmentTableView.getItems().clear();
        try {
            appointments = this.apiAppointmentService.getAllAppointmentsAdmin();
            invitations = this.apiInvitationService.getInvitations();
            appointmentTableView.getItems().addAll(appointments);

        } catch (Exception e) {
            ErrorsAndInfos.showError("Could not Refresh", "Error", e.getLocalizedMessage());
        }
        appointmentTableView.refresh();
    }


    public void goBack() throws IOException {
        appRouter.popRoute();
    }
}
