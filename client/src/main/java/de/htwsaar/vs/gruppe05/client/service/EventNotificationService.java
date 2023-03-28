package de.htwsaar.vs.gruppe05.client.service;


import de.htwsaar.vs.gruppe05.client.ClientApplication;
import de.htwsaar.vs.gruppe05.client.authentication.JwtStorage;
import de.htwsaar.vs.gruppe05.client.model.Invitation;
import javafx.application.Platform;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

public class EventNotificationService {

    private List<Invitation> invitations = new ArrayList<>();
    private static EventNotificationService INSTANCE;
    private Disposable disposable;
    private Flux<ServerSentEvent<List<Invitation>>> invite = null;

    private boolean newInvitations = false;

    public static EventNotificationService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EventNotificationService();
        }

        return INSTANCE;
    }

    private EventNotificationService() {
        startScheduledTask();
    }

    private void startScheduledTask() {
        System.out.println("Started SSE fetching");
        Platform.runLater(() -> {
            ParameterizedTypeReference<ServerSentEvent<List<Invitation>>> type = new ParameterizedTypeReference<ServerSentEvent<List<Invitation>>>() {
            };
            List map = new ArrayList<>();
            JwtStorage storage = (JwtStorage) ClientApplication.getApplicationContext().getBean("jwtStorage");
            WebClient webClient = WebClient.builder().build();
            invite = webClient.get()
                    .uri("http://localhost:8080/api/invitations-sse")
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + storage.getToken())
                    .retrieve()
                    .bodyToFlux(type);

            subscribe();
        });


    }

    public void restart() {
        dispose();
        startScheduledTask();
    }

    private void subscribe() {
        disposable = invite.subscribe(content -> {
            if (invitations.size() < content.data().size()) {
                for (Invitation invitation : content.data()) {
                    if (!invitations.contains(invitation)) {
                        invitations.add(invitation);
                        setNewInvitations(true);
                    }
                }
            }
        });
    }

    public void dispose() {
        disposable.dispose();
    }

    public boolean isNewInvitations() {
        return newInvitations;
    }

    public void setNewInvitations(boolean newInvitations) {
        this.newInvitations = newInvitations;
    }
}
