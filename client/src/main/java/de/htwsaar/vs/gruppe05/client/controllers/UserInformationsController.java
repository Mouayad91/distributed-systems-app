package de.htwsaar.vs.gruppe05.client.controllers;

import de.htwsaar.vs.gruppe05.client.model.User;
import de.htwsaar.vs.gruppe05.client.routing.AppRouter;
import de.htwsaar.vs.gruppe05.client.service.ApiUserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UserInformationsController {


    @FXML
    private Label userNameLbl;

    @FXML
    private Label idLbl;

    @FXML
    private Label emailLbl;


    @FXML
    private ApiUserService apiUserService;

    @Autowired
    private AppRouter appRouter;

    private User user;

    private User selectedUser;


    public void setUser(User user) {
        this.user = user;

        userNameLbl.setText(user.getUserName());
        idLbl.setText(Long.toString(user.getId()));
        emailLbl.setText(user.getEmail());


    }

    @FXML
    void back(ActionEvent event) throws Exception {
        appRouter.popRoute();
    }

    public void setSelectedUser(User user) {
        this.selectedUser = user;
    }

    public User getSelectedUser() {
        return selectedUser;
    }


    void goToAllUsersView() throws IOException {
        appRouter.popRoute();
    }


}
