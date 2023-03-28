package de.htwsaar.vs.gruppe05.client.routing;

import de.htwsaar.vs.gruppe05.client.ClientApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Global App Router Singleton
 * <p>
 * Provides different ways of routing between scenes
 *
 * @version 16.03.2023
 */
@Component
public class AppRouter {

    private static AppRouter instance;

    /**
     * The list of routes, initial empty
     */
    List<String> routes = new ArrayList<>();

    private AppRouter() {

    }

    public static AppRouter getInstance() {

        if (instance == null) {
            instance = new AppRouter();
        }

        return instance;
    }


    /**
     * Navigate back to a Scene  ( in list ) and close the showed page
     *
     * @throws IOException
     */
    public void popRoute() throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(routes.get(routes.size() - 1)));
        loader.setControllerFactory(ClientApplication.getApplicationContext()::getBean);
        Parent root = null;
        root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(false);
        stage.show();
        routes.remove(routes.size() - 1);
        List<Window> open = Stage.getWindows().stream().filter(Window::isShowing).toList();
        Stage closingStage = (Stage) open.get(0);
        closingStage.close();
    }


    /**
     * Navigate to @route | continue showing the current window | do not add Route to list (forward)
     *
     * @param route
     * @throws IOException
     */
    public void goToRoute(String route) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(route));
        loader.setControllerFactory(ClientApplication.getApplicationContext()::getBean);
        Parent root = null;
        root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(false);
        stage.show();
    }

    /**
     * Navigate to @route | close the current Scene | do not add route to list
     *
     * @param route
     * @throws IOException
     */
    public void goToRoutePop(String route) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(route));
        loader.setControllerFactory(ClientApplication.getApplicationContext()::getBean);
        Parent root = null;
        root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(false);
        stage.show();
        List<Window> open = Stage.getWindows().stream().filter(Window::isShowing).toList();
        Stage closingStage = (Stage) open.get(0);
        closingStage.close();
    }


    /**
     * Navigate to a route and add to list
     *
     * @param route
     * @throws IOException
     */
    public void pushToRoute(String route) throws IOException {
        this.pushToRoute(route, "");
    }

    /**
     * Navigate to route and provide a origin | add to list
     *
     * @param route
     * @param origin
     * @throws IOException
     */
    public void pushToRoute(String route, String origin) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(route));
        loader.setControllerFactory(ClientApplication.getApplicationContext()::getBean);
        Parent root = null;
        root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(false);
        stage.show();
        List<Window> open = Stage.getWindows().stream().filter(Window::isShowing).toList();
        Stage closingStage = (Stage) open.get(0);
        if (routes.size() == 0) {
            if (!(origin == null && origin.isEmpty() && origin.isBlank())) {
                routes.add(origin);
            }
        }

        closingStage.close();
    }
}
