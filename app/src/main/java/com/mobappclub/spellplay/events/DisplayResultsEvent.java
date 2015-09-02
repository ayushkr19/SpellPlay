package com.mobappclub.spellplay.events;

import java.util.HashMap;

/**
 * Created by ayush on 29/08/15.
 */
public class DisplayResultsEvent {

    private int numWrongWords;
    private HashMap<String, Object> wrongWords;
    private int numWordsInRawInput;
    private  int numInvalidWords;

    public DisplayResultsEvent(int numWrongWords, HashMap<String, Object> wrongWords, int numWordsInRawInput, int numInvalidWords) {
        this.numWrongWords = numWrongWords;
        this.wrongWords = wrongWords;
        this.numWordsInRawInput = numWordsInRawInput;
        this.numInvalidWords = numInvalidWords;
    }

    public int getNumInvalidWords() {
        return numInvalidWords;
    }

    public void setNumInvalidWords(int numInvalidWords) {
        this.numInvalidWords = numInvalidWords;
    }

    public int getNumWordsInRawInput() {
        return numWordsInRawInput;
    }

    public void setNumWordsInRawInput(int numWordsInRawInput) {
        this.numWordsInRawInput = numWordsInRawInput;
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

