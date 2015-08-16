package com.mobappclub.spellplay.events;

/**
 * Created by ayush on 15/8/15.
 */
public class CheckWordsFromAPIEvent {

    private String words;

    public CheckWordsFromAPIEvent(String words) {
        this.words = words;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }
}
