package de.htwsaar.vs.gruppe05.client.controllers;

import de.htwsaar.vs.gruppe05.client.model.User;
import de.htwsaar.vs.gruppe05.client.model.enums.RoleEnums;
import de.htwsaar.vs.gruppe05.client.routing.AppRouter;
import de.htwsaar.vs.gruppe05.client.service.ApiUserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;

@Component
public class UpdateUserController {

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;


    @FXML
    private TextField newUserName;

    @FXML
    private TextField newEmail;

    @FXML
    private Button saveBtn;

    @FXML
    private Button backBtn;

    @FXML
    private TextField newPasswordReptxt;

    @FXML
    private TextField newPasswordtxt;

    @FXML
    private ChoiceBox choiceBox;

    private User selectedUser;

    @Autowired
    private ApiUserService apiUserService;
    @Autowired
    private AppRouter appRouter;

    public void setUser(User user) {
        this.selectedUser = user;
        this.firstName.setText(user.getFirstName());
        this.lastName.setText(user.getLastName());
        this.newUserName.setText(user.getUserName());
        this.newPasswordtxt.setText(user.getPassword());
        this.newPasswordReptxt.setText(user.getPassword());
        this.newEmail.setText(user.getEmail());
        this.choiceBox.setValue(user.getRole().name());

    }

    @FXML
    public void updateUser(ActionEvent actionEvent) throws URISyntaxException, IOException, InterruptedException {

        if (newUserName.getText().isEmpty() || newUserName.getText().isBlank()) {
            ErrorsAndInfos.showError("Error", "An error has occurred", "Please enter a username.");
            return;
        }

        if (newEmail.getText().isEmpty() || newEmail.getText().isBlank()) {
            ErrorsAndInfos.showError("Error", "An error has occurred", "Please enter an email.");
            return;
        }


        if (firstName.getText().isEmpty() || firstName.getText().isBlank()) {
            ErrorsAndInfos.showError("Error", "An error has occurred", "Please enter a password.");
            return;
        }
        if (lastName.getText().isEmpty() || lastName.getText().isBlank()) {
            ErrorsAndInfos.showError("Error", "An error has occurred", "Please enter a password.");
            return;
        }

        if (choiceBox.getValue().equals("ADMIN")) {
            selectedUser.setRole(RoleEnums.Role.ADMIN);
        } else if (choiceBox.getValue().equals("USER")) {
            selectedUser.setRole(RoleEnums.Role.USER);
        } else {
            ErrorsAndInfos.showError("Error", "An error has occurred", "No Role selected");
            return;
        }

        selectedUser.setFirstName(firstName.getText());
        selectedUser.setLastName(lastName.getText());
        selectedUser.setUserName(newUserName.getText());
        selectedUser.setEmail(newEmail.getText());
        selectedUser.setPassword(newPasswordtxt.getText());

        boolean success = apiUserService.updateUserAdmin(selectedUser);

        if (!success) {
            // Show success message
            ErrorsAndInfos.showInformation("User updated successfully", "The user's information has been updated successfully.");
        } else {
            // Show error message
            ErrorsAndInfos.showError("Error", "An error has occurred", "The user's information could not be updated.");

        }
    }

    @FXML
    void back(ActionEvent actionEvent) throws IOException {
        appRouter.popRoute();
    }
}
