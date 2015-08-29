package com.mobappclub.spellplay.events;

/**
 * Created by ayush on 29/08/15.
 */
public class HighScoreFetchedEvent {
    String response;

    public HighScoreFetchedEvent(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
