package de.htwsaar.vs.gruppe05.client.controllers;

import de.htwsaar.vs.gruppe05.client.authentication.JwtStorage;
import de.htwsaar.vs.gruppe05.client.model.ApiResult;
import de.htwsaar.vs.gruppe05.client.model.Appointment;
import de.htwsaar.vs.gruppe05.client.model.Invitation;
import de.htwsaar.vs.gruppe05.client.model.User;
import de.htwsaar.vs.gruppe05.client.routing.AppRouter;
import de.htwsaar.vs.gruppe05.client.service.ApiAppointmentService;
import de.htwsaar.vs.gruppe05.client.service.ApiInvitationService;
import de.htwsaar.vs.gruppe05.client.service.ApiUserService;
import de.htwsaar.vs.gruppe05.client.service.Codes;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class CreateAppointmentController implements Initializable {

    @FXML
    private Button backBtn;

    @FXML
    private Button createAppointmentBtn;

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

    @Autowired
    private ApiAppointmentService appointmentService;

    @Autowired
    private ApiUserService apiUserService;

    @Autowired
    private AppRouter appRouter;

    @FXML
    private ListView<User> listfx;

    @Autowired
    private ApiAppointmentService apiAppointmentService;

    @Autowired
    private ApiInvitationService apiInvitationService;

    @FXML
    private JwtStorage jwtStorage;

    private List<User> userList = new ArrayList<>();

    private ObservableList<User> allUserObservableList;


    @FXML
    void back() throws Exception {
        appRouter.popRoute();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

    @FXML
    void createAppointment() throws IOException, URISyntaxException, InterruptedException {
        int starthour = (int) startHoursSpinner.getValue();
        int startminute = (int) startMinutesSpinner.getValue();
        int endhour = (int) endHoursSpinner.getValue();
        int endminute = (int) endMinutesSpinner.getValue();


        if (description.getText().isEmpty() || description.getText().isBlank()) {
            ErrorsAndInfos.showError("Error", "An error has occurred", "Please write a Description.");
            return;
        }

        if (titletxt.getText().isEmpty() || titletxt.getText().isBlank()) {
            ErrorsAndInfos.showError("Error", "Title is missing", "Please enter a Title");
            return;
        }

        if (starthour < 0 || startminute < 0 || startminute >= 60 || starthour >= 24) {
            ErrorsAndInfos.showError("Error", "Invalid start time", "Please enter a valid start time");
            return;
        }

        if (endhour < 0 || endminute < 0 || endhour >= 24 || endminute >= 60) {
            ErrorsAndInfos.showError("Error", "Invalid end time", "Please enter a valid end time");
            return;
        }

        if (date.getValue() == null || date.getValue().isBefore(LocalDate.now())) {
            ErrorsAndInfos.showError("Error", "Invalid date", "Please select a date that is not in the past.");
            return;
        }

        if (locationfx.getText().isBlank() || locationfx.getText().isEmpty()) {
            ErrorsAndInfos.showError("Error", "No Location defined", "Please enter a location");
        }


        ApiResult<Appointment> apiResult = appointmentService.createAppointment(new Appointment(titletxt.getText(), description.getText(), locationfx.getText(), LocalDateTime.of(date.getValue(), LocalTime.of(starthour, startminute)), LocalDateTime.of(date.getValue(), LocalTime.of(endhour, endminute))));
        System.out.println(apiResult.getResponseContent());
        if (apiResult.getCode().equals(Codes.StatusCode.SUCCESS)) {
            for (User user : userList) {
                ApiResult invitationApiResult = apiInvitationService.createInvitation(new Invitation(user.getId(), apiResult.getResponseContent().getId()));
                if (invitationApiResult.getCode().equals(Codes.StatusCode.CONFLICT)) {
                    ErrorsAndInfos.showInformation("Collision detected", "Appointment for" + user.getUserName() + "\n" + invitationApiResult.getResponseContent() + titletxt.getText());
                }
            }
            Optional<ButtonType> noticedButton = ErrorsAndInfos.showInformation("Success", "Successfully create appointment " + titletxt.getText());
            if (noticedButton.isPresent()) {
                appRouter.popRoute();
            }
        }
    }
}