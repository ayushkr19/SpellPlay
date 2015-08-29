package com.mobappclub.spellplay.events;

import com.mobappclub.spellplay.util.Constants;

/**
 * Created by ayush on 29/08/15.
 */
public class FetchHighScoresEvent {
    private String url;

    public FetchHighScoresEvent(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
