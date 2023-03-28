package de.htwsaar.vs.gruppe05.client.controllers;

import de.htwsaar.vs.gruppe05.client.authentication.JwtStorage;
import de.htwsaar.vs.gruppe05.client.model.User;
import de.htwsaar.vs.gruppe05.client.routing.AppRouter;
import de.htwsaar.vs.gruppe05.client.service.ApiUserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;

@Component
public class EditAccountController {


    @FXML
    private TextField newUserName;

    @FXML
    private TextField newEmail;


    @FXML
    private TextField newPasswordReptxt;

    @FXML
    private TextField newPasswordtxt;

    @FXML
    private TextField newFirstNametxt;

    @FXML
    TextField newLastNametxt;

    @FXML
    private Label userNameLbl;

    @FXML
    private Label idLbl;

    @FXML
    private Label emailLbl;


    @Autowired
    private AppRouter appRouter;


    @Autowired
    public JwtStorage jwtStorage;

    private User currentUser;
    @Autowired
    private ApiUserService apiUserService;

//    public long id = jwtStorage.getUserId();
//    private User currentUser = apiUserService.getUserById(id);

    public EditAccountController() throws URISyntaxException, IOException, InterruptedException {
    }


    @FXML
    public void initialize() throws URISyntaxException, IOException, InterruptedException {
        long id = jwtStorage.getUserId();
        this.currentUser = apiUserService.getUserById(id);
        System.out.println(currentUser);

        userNameLbl.setText(currentUser.getUserName());
        idLbl.setText(Long.toString(currentUser.getId()));
        emailLbl.setText(currentUser.getEmail());
    }


    @FXML
    public void edit(ActionEvent actionEvent) throws URISyntaxException, IOException, InterruptedException {
        long id = jwtStorage.getUserId();
        User currentUser = apiUserService.getUserById(id);
        if (newUserName.getText().isEmpty() || newUserName.getText().isBlank()) {
            ErrorsAndInfos.showError("Error", "An error has occurred", "Please enter a username.");
            return;
        }

        if (newEmail.getText().isEmpty() || newEmail.getText().isBlank()) {
            ErrorsAndInfos.showError("Error", "An error has occurred", "Please enter an email.");
            return;
        }

        if (newPasswordtxt.getText().isEmpty() || newPasswordtxt.getText().isBlank()) {
            ErrorsAndInfos.showError("Error", "An error has occurred", "Please enter a password.");
            return;
        }

        if (!newPasswordtxt.getText().equals(newPasswordReptxt.getText())) {
            ErrorsAndInfos.showError("Error", "An error has occurred", "The entered passwords do not match.");
            return;
        }

        if (newFirstNametxt.getText().isEmpty() || newFirstNametxt.getText().isBlank()) {
            ErrorsAndInfos.showError("Error", "An error has occurred", "Please enter a first name.");
            return;
        }

        if (newLastNametxt.getText().isEmpty() || newLastNametxt.getText().isBlank()) {
            ErrorsAndInfos.showError("Error", "An error has occurred", "Please enter a last name.");
            return;
        }

        // Update the current user's information
        currentUser.setUserName(newUserName.getText());
        currentUser.setEmail(newEmail.getText());
        currentUser.setPassword(newPasswordtxt.getText());
        currentUser.setFirstName(newFirstNametxt.getText());
        currentUser.setLastName(newLastNametxt.getText());

        // Send HTTP PUT request to update user on the server
        boolean success = apiUserService.updateUser(currentUser);

        if (success) {
            // Show success message
            ErrorsAndInfos.showInformation("User updated successfully", "The user's information has been updated successfully.");
        } else {
            // Show error message
            ErrorsAndInfos.showError("Error", "An error has occurred", "The user's information could not be updated.");
        }
    }


//    @FXML
//    void back() throws IOException {
////        long id = jwtStorage.getUserId();
////        System.out.println("The Id is:: " + id);
//        appRouter.popRoute();
//
//    }

    @FXML
    void back() throws Exception {
        appRouter.popRoute();
    }
}