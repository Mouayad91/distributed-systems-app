package de.htwsaar.vs.gruppe05.client.controllers;

import de.htwsaar.vs.gruppe05.client.ClientApplication;
import de.htwsaar.vs.gruppe05.client.model.Appointment;
import de.htwsaar.vs.gruppe05.client.model.Invitation;
import de.htwsaar.vs.gruppe05.client.model.User;
import de.htwsaar.vs.gruppe05.client.routing.AppRouter;
import de.htwsaar.vs.gruppe05.client.service.ApiAppointmentService;
import de.htwsaar.vs.gruppe05.client.service.ApiInvitationService;
import de.htwsaar.vs.gruppe05.client.service.ApiUserService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class AppointmentViewController {

    @FXML
    private TextField titletxt;

    @FXML
    private DatePicker date;

    @FXML
    private TextArea description;

    @FXML
    private Spinner startHoursSpinner;

    @FXML
    private Spinner startMinutesSpinner;

    @FXML
    private Spinner endHoursSpinner;

    @FXML
    private Spinner endMinutesSpinner;


    @FXML
    private TextField locationfx;

    @FXML
    private Button closeBtn;

    @FXML
    private Button editBtn;

    @FXML
    private ListView<User> listfx;

    @FXML
    private Button invStatusButton;

    @Autowired
    private AppRouter appRouter;

    @Autowired
    private ApiAppointmentService apiAppointmentService;

    @Autowired
    private ApiUserService apiUserService;

    @Autowired
    private ApiInvitationService apiInvitationService;

    private Appointment appointment;

    private List<User> userList = new ArrayList<>();

    private ObservableList<User> allUserObservableList;

    @FXML
    public void initialize() {
        try {
            List<User> users = apiUserService.getAllUsers();
            allUserObservableList = FXCollections.observableArrayList();
            allUserObservableList.addAll(users);
            listfx.setItems(allUserObservableList);
        } catch (Exception e) {
            ErrorsAndInfos.showError("Error", "Getting Users", "users list not fetchable");
        }
        listfx.setCellFactory(param -> new ListCell<User>() {
            @Override
            protected void updateItem(User user, boolean b) {
                super.updateItem(user, b);
                if (b || user == null || user.getUserName() == null) {
                    setText("");
                } else {
                    setText(user.getUserName());
                }
            }
        });
        listfx.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listfx.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>() {
            @Override
            public void changed(ObservableValue<? extends User> observableValue, User user, User t1) {
                updateUserList();
                System.out.println(userList);
            }
        });
    }

    public void updateUserList() {
        ObservableList<User> selectedUsers = listfx.getSelectionModel().getSelectedItems();
        userList = selectedUsers.stream().toList();
    }


    public void initAppointment(Appointment appointment) {
        this.appointment = appointment;
        titletxt.setText(appointment.getTitle());
        date.setValue(LocalDate.of(appointment.getStartTime().getYear(), appointment.getStartTime().getMonth(), appointment.getStartTime().getDayOfMonth()));
        description.setText(appointment.getDescription());
        startHoursSpinner.getValueFactory().setValue(appointment.getStartTime().getHour());
        startMinutesSpinner.getValueFactory().setValue(appointment.getStartTime().getMinute());
        endHoursSpinner.getValueFactory().setValue(appointment.getEndTime().getHour());
        endMinutesSpinner.getValueFactory().setValue(appointment.getEndTime().getMinute());
        locationfx.setText(appointment.getLocation());

        allUserObservableList.removeIf(user -> user.getId() == appointment.getUserId());

        try {
            List<User> invitedUsers = apiAppointmentService.getAllInvitedUserToAppointment(appointment.getId());
            for (User userList : invitedUsers) {
                for (User user : allUserObservableList) {
                    if (userList.getId() == user.getId()) {
                        listfx.getSelectionModel().select(user);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disableFields() {
        titletxt.setDisable(true);
        date.setDisable(true);
        description.setDisable(true);
        startHoursSpinner.setDisable(true);
        startMinutesSpinner.setDisable(true);
        endHoursSpinner.setDisable(true);
        endMinutesSpinner.setDisable(true);
        locationfx.setDisable(true);
        listfx.setDisable(true);
    }

    public void enableEditButton() {
        editBtn.setVisible(true);
    }

    @FXML
    private void closeButtonAction() {
        Stage stage = (Stage) closeBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void editButtonAction() {
        int starthour = (int) startHoursSpinner.getValue();
        int startminute = (int) startMinutesSpinner.getValue();
        int endhour = (int) endHoursSpinner.getValue();
        int endminute = (int) endMinutesSpinner.getValue();


        appointment.setDescription(description.getText());
        appointment.setTitle(titletxt.getText());
        appointment.setLocation(locationfx.getText());
        appointment.setStartTime(LocalDateTime.of(date.getValue(), LocalTime.of(starthour, startminute)));
        appointment.setEndTime(LocalDateTime.of(date.getValue(), LocalTime.of(endhour, endminute)));

        try {
            apiAppointmentService.updateAppointment(appointment);
            for (User user : userList) {
                apiInvitationService.createInvitation(new Invitation(user.getId(), appointment.getId()));
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Stage stage = (Stage) closeBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void handleInvitationButtonPress() {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ShowInvitedUsersView.fxml"));
        loader.setControllerFactory(ClientApplication.getApplicationContext()::getBean);

        Parent root = null;
        try {
            root = loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(false);
        //load appointment into next view
        InvitedUsersController invitedUsersController = loader.getController();
        invitedUsersController.initInvitations(appointment);
        stage.showAndWait();
    }


}
