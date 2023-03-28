package de.htwsaar.vs.gruppe05.client;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"de.htwsaar.vs.gruppe05.client"})
public class App {

    public static void main(String[] args) {
        Application.launch(ClientApplication.class, args);
    }
}
