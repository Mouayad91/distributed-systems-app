package de.htwsaar.vs.gruppe05.client.controllers;

import de.htwsaar.vs.gruppe05.client.routing.AppRouter;
import de.htwsaar.vs.gruppe05.client.service.ApiUserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RegisterController {

    @FXML
    private Button LoginBtn;

    @FXML
    private PasswordField passwordtxt;

    @FXML
    private TextField usernametxt;


    @FXML
    private TextField emaitxt;

    @FXML
    private TextField firstNametxt;

    @FXML
    private TextField lastNametxt;

    @FXML
    private Button backBtn;

    @Autowired
    private ApiUserService apiUserService;

    @Autowired
    private AppRouter appRouter;

    public void goBack() throws IOException {
        appRouter.goToRoutePop("/LoginView.fxml");
    }

    public void register(ActionEvent event) throws Exception {
        boolean usernameEmpty = false;
        boolean firstNameEmpty = false;
        boolean lastNameEmpty = false;
        boolean emailEmpty = false;
        boolean passwordEmpty = false;

        if (usernametxt.getText().isEmpty() || usernametxt.getText().isBlank()) {
            usernameEmpty = true;
            showError("username");

        }
        if (passwordtxt.getText().isEmpty() || passwordtxt.getText().isBlank()) {
            passwordEmpty = true;
            showError("password");
        }
        if (lastNametxt.getText().isEmpty() || lastNametxt.getText().isBlank()) {
            lastNameEmpty = true;
            showError("last name");
        }
        if (firstNametxt.getText().isEmpty() || firstNametxt.getText().isBlank()) {
            firstNameEmpty = true;
            showError("first mame");
        }
        if (emaitxt.getText().isEmpty() || emaitxt.getText().isBlank()) {
            emailEmpty = true;
            showError("e-mail address");
        }

        // Get the entered username and password
        String username = usernametxt.getText();
        String password = passwordtxt.getText();
        String email = emaitxt.getText();
        String firstName = firstNametxt.getText();
        String lastName = lastNametxt.getText();

        Boolean hasRegistered = apiUserService.register(firstName, lastName, email, username, password);

        if (!hasRegistered) {
            showError("Username/Password Combination does not match"); // fix Registration
            return;
        }
        goHome();
    }

    private void showError(String message) {
        ErrorsAndInfos.showError("Error", "An error has occurred", "Please enter your " + message);
    }


    public void closeScene() {
        Stage logInStage = (Stage) backBtn.getScene().getWindow();
        logInStage.close();
    }


    private void goHome() throws IOException {
        appRouter.goToRoutePop("/UserIndexView.fxml");
    }


}
