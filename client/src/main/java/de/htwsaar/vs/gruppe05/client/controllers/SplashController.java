package de.htwsaar.vs.gruppe05.client.controllers;

import de.htwsaar.vs.gruppe05.client.routing.AppRouter;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SplashController {

    @Autowired
    private AppRouter appRouter;

    @FXML
    public void initialize() {
        goLogin();

    }


    private void goLogin() {
        Timeline timeline = new Timeline();
        KeyFrame kf = new KeyFrame(Duration.seconds(1),
                event -> {
                    try {
                        appRouter.goToRoutePop("/LoginView.fxml");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    timeline.stop();
                });
        timeline.getKeyFrames().add(kf);
        timeline.play();
    }

}
