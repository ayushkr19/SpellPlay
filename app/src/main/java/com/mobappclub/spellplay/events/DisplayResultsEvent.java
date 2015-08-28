package com.mobappclub.spellplay.events;

import java.util.HashMap;

/**
 * Created by ayush on 29/08/15.
 */
public class DisplayResultsEvent {

    private int numWrongWords;
    private HashMap<String, Object> wrongWords;

    public DisplayResultsEvent(int numWrongWords, HashMap<String, Object> wrongWords) {
        this.numWrongWords = numWrongWords;
        this.wrongWords = wrongWords;
    }

    public int getNumWrongWords() {
        return numWrongWords;
    }

    public void setNumWrongWords(int numWrongWords) {
        this.numWrongWords = numWrongWords;
    }

    public HashMap<String, Object> getWrongWords() {
        return wrongWords;
    }

    public void setWrongWords(HashMap<String, Object> wrongWords) {
        this.wrongWords = wrongWords;
    }
}

