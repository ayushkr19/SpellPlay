package com.mobappclub.spellplay.events;

/**
 * Created by ayush on 15/8/15.
 */
public class CheckWordsFromAPIEvent {

    private String words;
    private char startLetter;

    public CheckWordsFromAPIEvent(String words, char startLetter) {
        this.words = words;
        this.startLetter = startLetter;
    }

    public char getStartLetter() {
        return startLetter;
    }

    public void setStartLetter(char startLetter) {
        this.startLetter = startLetter;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }
}
