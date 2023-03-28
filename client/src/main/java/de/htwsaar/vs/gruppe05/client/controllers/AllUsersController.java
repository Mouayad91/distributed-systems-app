package de.htwsaar.vs.gruppe05.client.controllers;

import com.google.gson.Gson;
import de.htwsaar.vs.gruppe05.client.ClientApplication;
import de.htwsaar.vs.gruppe05.client.authentication.JwtStorage;
import de.htwsaar.vs.gruppe05.client.model.User;
import de.htwsaar.vs.gruppe05.client.routing.AppRouter;
import de.htwsaar.vs.gruppe05.client.service.ApiUserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Component
public class AllUsersController {

    @FXML
    private TableView<User> allUsersTable;

    @FXML
    private TableColumn<User, String> userName;

    @FXML
    private TableColumn<User, String> email;
    @FXML
    private Button backBtn;

    @FXML
    private Button showUserBtn;

    @Autowired
    private AppRouter appRouter;


    @Autowired
    private ApiUserService apiUserService;

    private List<User> users = new ArrayList<>();


    @FXML
    public void deleteUser() throws URISyntaxException, IOException, InterruptedException {
        User selectedUser = allUsersTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            ErrorsAndInfos.showError("Error", "No User Selected", "Please select a user from the table.");
            return;
        }
        apiUserService.deleteUserById(selectedUser.getId());
        users.remove(selectedUser);
        allUsersTable.getItems().setAll(users);
        allUsersTable.refresh();
    }

    @FXML
    public void initialize() throws URISyntaxException, IOException, InterruptedException {
        // Set up the table columns
        userName.setCellValueFactory(new PropertyValueFactory<User, String>("userName"));
        email.setCellValueFactory(new PropertyValueFactory<User, String>("email"));


        try {
            users = this.apiUserService.getAllUsers();
            allUsersTable.getItems().setAll(users);


        } catch (IOException ex) {
            System.out.println(ex);
        } catch (URISyntaxException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

//        populateTable();

//    @FXML
//    public void populateTable() {
//        // Initialize the user list and populate the table
//        allUsersList = FXCollections.observableArrayList();
//        allUsersList.addAll(fetchUsers());
//        allUsersTable.setItems(allUsersList);
//    }

    @FXML
    private void showUser(ActionEvent event) throws IOException {
        // Get the selected user from the table
        User selectedUser = allUsersTable.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {
            ErrorsAndInfos.showError("Error", "No User Selected", "Please select a user from the table.");
            return;
        }
        // Set up the new stage and load the UserInformationsView
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInformationsView.fxml"));
        loader.setControllerFactory(ClientApplication.getApplicationContext()::getBean);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(false);

        // Pass the selected user's information to the UserInformationsController
        UserInformationsController controller = loader.getController();
        controller.setUser(selectedUser);

        // Show the new stage and close the current stage
        stage.show();
        Stage currentStage = (Stage) showUserBtn.getScene().getWindow();
        currentStage.close();
    }


    @FXML
    void back(ActionEvent event) throws Exception {
        appRouter.popRoute();
    }


    @FXML
    public List<User> fetchUsers() {
        List<User> userList = new ArrayList<>();
        try {
            // create a new instance of ApiUserService
            ApiUserService apiUserService = new ApiUserService(new JwtStorage(), new Gson());

            // get all the users from the server
            List<User> fetchedUserList = apiUserService.getAllUsers();

            // loop through all the users and add their usernames and id's to the userList
            for (User user : fetchedUserList) {
                userList.add(user);
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } catch (URISyntaxException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        return userList;
    }

    @FXML
    private void updateUser() throws IOException {


        User selectedUser = allUsersTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            ErrorsAndInfos.showError("Error", "No User Selected", "Please select a user from the table.");
            return;
        }

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateUserView.fxml"));
        loader.setControllerFactory(ClientApplication.getApplicationContext()::getBean);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(false);

        // Pass the selected user's information to the UserInformationsController
        UpdateUserController controller = loader.getController();
        controller.setUser(selectedUser);

        // Show the new stage and close the current stage
        stage.show();
        Stage currentStage = (Stage) showUserBtn.getScene().getWindow();
        currentStage.close();

    }


}