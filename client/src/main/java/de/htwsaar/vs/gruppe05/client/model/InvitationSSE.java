package de.htwsaar.vs.gruppe05.client.model;

import java.util.List;

public class InvitationSSE {

    private String id;

    private String event;

    private List<Invitation> data;


    public InvitationSSE() {
    }

    public List<Invitation> getData() {
        return data;
    }

    public String getEvent() {
        return event;
    }

    public String getId() {
        return id;
    }

    public void setData(List<Invitation> data) {
        this.data = data;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
