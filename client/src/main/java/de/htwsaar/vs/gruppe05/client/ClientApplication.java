package de.htwsaar.vs.gruppe05.client;

import de.htwsaar.vs.gruppe05.client.controllers.ErrorsAndInfos;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Stack;

public class ClientApplication extends Application {

    private static Stack<Object> previousControllers = new Stack<>();

    private static ConfigurableApplicationContext applicationContext;

    public static void main(final String[] args) {
        Application.launch(args);
    }


    @Override
    public void init() throws Exception {
        applicationContext = new SpringApplicationBuilder(App.class).run();
        System.out.println(Arrays.toString(applicationContext.getBeanDefinitionNames()));
    }

    @Override
    public void start(Stage stage) {
        if (!pingHost()) {
            ErrorsAndInfos.showError("Not Reachable", "Server is not reachable", "Please start the server or try again later");
            return;
        }
        stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SplashView.fxml"));
        loader.setControllerFactory(ClientApplication.getApplicationContext()::getBean);
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(false);
        stage.show();

    }

    @Override
    public void stop() throws Exception {
        applicationContext.close();
        Platform.exit();
    }

    public static ConfigurableApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getPreviousController() {
        if (previousControllers.isEmpty()) {
            return null;
        }
        return previousControllers.peek();
    }

    /**
     * Method that pings our localhost:8080 server
     * If the ping was successful we return true
     * If the ping failed we return false which is either because of a
     * timeout, unreachable or failed DNS Lookup on the side of the server
     *
     * @return true or false based on ping response
     */
    public static boolean pingHost() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(8080), 10000);
            return true;
        } catch (IOException e) {
            return false; // Either timeout or unreachable or failed DNS lookup.
        }
    }
}
