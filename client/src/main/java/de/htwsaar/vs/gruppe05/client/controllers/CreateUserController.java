package de.htwsaar.vs.gruppe05.client.controllers;

import de.htwsaar.vs.gruppe05.client.ClientApplication;
import de.htwsaar.vs.gruppe05.client.model.ApiResult;
import de.htwsaar.vs.gruppe05.client.model.User;
import de.htwsaar.vs.gruppe05.client.model.enums.RoleEnums;
import de.htwsaar.vs.gruppe05.client.routing.AppRouter;
import de.htwsaar.vs.gruppe05.client.service.ApiUserService;
import de.htwsaar.vs.gruppe05.client.service.Codes;
import javafx.event.ActionEvent;
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
import java.util.Optional;

@Component
public class CreateUserController {

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;


    @FXML
    private TextField usernametxt;

    @FXML
    private TextField emailtxt;

    @FXML
    private PasswordField passwordtxt;

    @FXML
    private Button backBtn;

    @FXML
    private ChoiceBox roleChoiceBox;

    @FXML
    private Button createUserBtn;

    @Autowired
    private ApiUserService apiUserService;

    @Autowired
    private AppRouter appRouter;


    @FXML
    void back(ActionEvent event) throws Exception {

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminIndexView.fxml"));
        loader.setControllerFactory(ClientApplication.getApplicationContext()::getBean);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(false);
        stage.show();
        Stage AllUSersViewStage = (Stage) backBtn.getScene().getWindow();
        AllUSersViewStage.close();
    }

    @FXML
    public void createUser(ActionEvent actionEvent) throws IOException, URISyntaxException, InterruptedException {
        if (firstName.getText().isEmpty() || firstName.getText().isBlank()) {
            ErrorsAndInfos.showError("Error", "First name is missing", "Please enter a name");
            return;
        }
        if (lastName.getText().isEmpty() || lastName.getText().isBlank()) {
            ErrorsAndInfos.showError("Error", "Last name is missing", "Please enter a name");
            return;
        }
        if (usernametxt.getText().isEmpty() || usernametxt.getText().isBlank()) {
            ErrorsAndInfos.showError("Error", "Name is missing", "Please enter a name");
            return;
        }


        if (emailtxt.getText().isEmpty() || emailtxt.getText().isBlank()) {
            ErrorsAndInfos.showError("Error", "Email is missing", "Please enter an Email");
            return;
        }

        if (passwordtxt.getText().isEmpty() || passwordtxt.getText().isBlank()) {
            ErrorsAndInfos.showError("Error", "Password is missing", "Please enter a password");
        }

        if (roleChoiceBox.getValue() == null) {
            // User has not selected a role, show an error message
            ErrorsAndInfos.showError("Error", "Role is missing", "Please select a role");
            return;
        }


        User user = new User(usernametxt.getText());
        if (roleChoiceBox.getValue().equals("Admin")) {
            user.setRole(RoleEnums.Role.ADMIN);
        } else {
            user.setRole(RoleEnums.Role.USER);
        }
        user.setEmail(emailtxt.getText());
        user.setPassword(passwordtxt.getText());
        user.setFirstName(firstName.getText());
        user.setLastName(lastName.getText());


        ApiResult status = apiUserService.createUserAdmin(user);
        if (status.getCode().equals(Codes.StatusCode.SUCCESS)) {
            Optional<ButtonType> okAction = ErrorsAndInfos.showInformation("Success", "Successfully created user with username " + usernametxt.getText());
            if (okAction.isPresent()) {
                appRouter.popRoute();
            }
        } else if (status.getCode().equals(Codes.StatusCode.UNAUTHORIZED)) {
            Optional<ButtonType> okAction = ErrorsAndInfos.showError("Login required", "Reauthorization needed", "You need to re-authorize again");
            if (okAction.isPresent()) {
                appRouter.goToRoutePop("/LoginView.fxml");
            }
        } else if (status.getCode().equals(Codes.StatusCode.FAILURE)) {
            Optional<ButtonType> okAction = ErrorsAndInfos.showError("Creation Error", "", status.getMessage());

        }
    }
}
