package de.htwsaar.vs.gruppe05.client.controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.springframework.util.ErrorHandler;

import java.util.Optional;

public class ErrorsAndInfos implements ErrorHandler {
    public static Optional<ButtonType> showError(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert.showAndWait();
    }


    static Optional<ButtonType> showInformation(String title, String headerText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        String contentText = null;
        alert.setContentText(contentText);
        return alert.showAndWait();
    }

    @Override
    public void handleError(Throwable t) {

    }
}
