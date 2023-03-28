package de.htwsaar.vs.gruppe05.client.controllers;

import de.htwsaar.vs.gruppe05.client.authentication.JwtStorage;
import de.htwsaar.vs.gruppe05.client.model.LoginResponse;
import de.htwsaar.vs.gruppe05.client.model.enums.RoleEnums;
import de.htwsaar.vs.gruppe05.client.routing.AppRouter;
import de.htwsaar.vs.gruppe05.client.service.ApiUserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;


@Component
public class LoginController {

    @FXML
    private Button LoginBtn;

    @FXML
    private PasswordField passwordtxt;

    @FXML
    private TextField usernametxt;

    @Autowired
    public JwtStorage jwtStorage;

    @Autowired
    private ApiUserService apiUserService;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private AppRouter appRouter;


    public void login(ActionEvent event) throws Exception {
        if (usernametxt.getText().isEmpty() || usernametxt.getText().isBlank()) {
            ErrorsAndInfos.showError("Error", "An error has occurred", "Please enter your Username.");
            return;
        }
        if (passwordtxt.getText().isEmpty() || passwordtxt.getText().isBlank()) {
            ErrorsAndInfos.showError("Error", "An error has occurred", "Please enter your Password.");
            return;
        }

        // Get the entered username and password
        String username = usernametxt.getText();
        String password = passwordtxt.getText();

        try {
            LoginResponse response = apiUserService.login(username, password);
            if (response.getToken() == null || response.getToken().isBlank() || response.getToken().isEmpty()) {
                ErrorsAndInfos.showError("Error", "An error has occurred", "Wrong username or password");

            }
            if (jwtStorage.isTokenSet()) {
                // Fix Get role (Why role == null??!!)
                if (jwtStorage.getRole().equals(RoleEnums.Role.ADMIN)) {
                    goToAdminIndexView();
                } else if (jwtStorage.getRole().equals(RoleEnums.Role.USER)) {
                    goToUserIndexView();
                }
            } else {
                ErrorsAndInfos.showError("Error", "An error has occurred", "JWT token is not set");
            }

        } catch (HttpClientErrorException e) {

            // Handle HTTP errors with the corresponding status codes
            if (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
                ErrorsAndInfos.showError("Error", "An error has occurred", "Invalid username or password");
            } else if (e.getStatusCode().equals(HttpStatus.FORBIDDEN)) {
                ErrorsAndInfos.showError("Error", "An error has occurred", "Access to the requested resource is forbidden");
            } else if (e.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                ErrorsAndInfos.showError("Error", "An error has occurred", "The request could not be understood or was missing required parameters");
            }

        }
    }


    public void goToRegister() throws IOException {
        appRouter.goToRoutePop("/RegisterView.fxml");
    }

    public void goToAdminIndexView() throws IOException {
        appRouter.goToRoutePop("/AdminIndexView.fxml");
    }

    public void goToUserIndexView() throws IOException {
        appRouter.goToRoutePop("/UserIndexView.fxml");
    }

}
