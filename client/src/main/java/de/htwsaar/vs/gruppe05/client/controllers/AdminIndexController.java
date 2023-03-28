package de.htwsaar.vs.gruppe05.client.controllers;

import de.htwsaar.vs.gruppe05.client.ClientApplication;
import de.htwsaar.vs.gruppe05.client.model.Appointment;
import de.htwsaar.vs.gruppe05.client.model.Invitation;
import de.htwsaar.vs.gruppe05.client.model.enums.StatusEnums;
import de.htwsaar.vs.gruppe05.client.routing.AppRouter;
import de.htwsaar.vs.gruppe05.client.service.ApiAppointmentService;
import de.htwsaar.vs.gruppe05.client.service.ApiInvitationService;
import de.htwsaar.vs.gruppe05.client.service.ApiUserService;
import de.htwsaar.vs.gruppe05.client.service.EventNotificationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
//import org.springframework.dao.DataAccessException;

@Component
public class AdminIndexController {


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
    private TableColumn<Invitation, StatusEnums.InvitationStatus> status;

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

    @FXML
    private Button invitationsBtn;

    @FXML
    private Circle notifyDot;

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

//    @FXML
//    private TableView<Invitation> appointmentTable;

    @FXML
    public void initialize() throws URISyntaxException, IOException, InterruptedException {
        // Set up the table columns
        title.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
        inviter.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("id"));
        startDate.setCellValueFactory(new PropertyValueFactory<Appointment, LocalDateTime>("startTime"));
        endDate.setCellValueFactory(new PropertyValueFactory<Appointment, LocalDateTime>("endTime"));
        lct.setCellValueFactory(new PropertyValueFactory<Appointment, String>("location"));
        description.setCellValueFactory(new PropertyValueFactory<Appointment, String>("description"));
        status.setCellValueFactory(new PropertyValueFactory<Invitation, StatusEnums.InvitationStatus>("status"));
        creationDate.setCellValueFactory(new PropertyValueFactory<Appointment, LocalDateTime>("creationDate"));
        lastEditDate.setCellValueFactory(new PropertyValueFactory<Appointment, LocalDateTime>("lastEditDate"));
        initContextMenu();
        loadData();
        initInviteSSE();


    }

    private void initInviteSSE() {
        EventNotificationService notificationService = EventNotificationService.getInstance();
        System.out.println("Started invitation button change on event");
        new Thread() {
            @Override
            public void run() {
                for (; ; ) {
                    if (notificationService.isNewInvitations()) {
                        invitationsBtn.setStyle("-fx-border-color: red;");
                        notifyDot.setVisible(true);
                    }
                }
            }
        }.start();
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
        menuItemEdit.setOnAction(this::editContextHandler);
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
        appointmentViewController.enableEditButton();
        stage.showAndWait();
        loadData();
    }

    public void loadData() {
        appointmentTableView.getItems().clear();
        try {
            appointments = this.apiAppointmentService.getAllAppointments();
            invitations = this.apiInvitationService.getInvitations();
            appointmentTableView.getItems().addAll(appointments);
            List<Appointment> appointmentsList = appointmentTableView.getItems();


            if (appointments.size() > 0) {
                for (int i = 0; i < appointments.size(); i++) {

                    int x = i;
                    // Check matching of i-th entry
                    appointments.forEach(e -> {
                        List<Invitation> matches = invitations.stream().filter(b -> b.getAppointmentId() == e.getId()).toList();
                        e.setStatus(matches.get(0).getStatus());
                    });
                }
            }
        } catch (Exception e) {
            ErrorsAndInfos.showError("Could not Refresh", "Error", e.getLocalizedMessage());
        }
        appointmentTableView.refresh();
    }


    public void getAllAppointments() throws IOException {
        appRouter.pushToRoute("/AdminAppointmentView.fxml", "/AdminIndexView.fxml");
    }


    public void goToInvitations() throws IOException {
        EventNotificationService.getInstance().setNewInvitations(false);
        appRouter.pushToRoute("/InvitationsView.fxml", "/AdminIndexView.fxml");
    }


    @FXML
    void showAllUsers() throws IOException {
        goToAllUsersView();

    }

    public void showLogs() throws IOException {
        appRouter.pushToRoute("/AdminLogView.fxml", "/AdminIndexView.fxml");
    }

    void goToAllUsersView() throws IOException {

        appRouter.pushToRoute("/AllUsersView.fxml", "/AdminIndexView.fxml");

    }

    public void createAppointment() throws IOException, URISyntaxException, InterruptedException {
        appRouter.pushToRoute("/CreateAppointmentView.fxml", "/AdminIndexView.fxml");
    }

    public void createUser(ActionEvent actionEvent) throws IOException {
        appRouter.pushToRoute("/CreateUserView.fxml", "/AdminIndexView.fxml");
    }

    public void showAllAppointments() throws IOException {
        appRouter.pushToRoute("/AdminAppointmentView.fxml", "/AdminIndexView.fxml");
    }

    @FXML
    void logout() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.setContentText("Click OK to confirm, or Cancel to stay on the current page.");


        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == ButtonType.OK) {
                EventNotificationService eventNotificationService = EventNotificationService.getInstance();
                eventNotificationService.dispose();

                appRouter.goToRoutePop("/LoginView.fxml");
            }
        }
    }

    private void closeScene() {
        Stage logInStage = (Stage) logoutBtn.getScene().getWindow();
        logInStage.close();
    }


}
