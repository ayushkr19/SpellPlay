package com.mobappclub.spellplay.events;

import com.mobappclub.spellplay.gson.HighScoreModel;

/**
 * Created by ayush on 29/08/15.
 */
public class HighScoreFetchedEvent {
    HighScoreModel highScoreModel;

    public HighScoreModel getHighScoreModel() {
        return highScoreModel;
    }

    public void setHighScoreModel(HighScoreModel highScoreModel) {
        this.highScoreModel = highScoreModel;
    }

    public HighScoreFetchedEvent(HighScoreModel highScoreModel) {
        this.highScoreModel = highScoreModel;
    }
}
