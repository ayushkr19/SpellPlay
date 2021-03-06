package com.mobappclub.spellplay.events;

/**
 * Created by ayush on 15/8/15.
 */
public class ResultFetchedEvent {

    private String result;
    private String error;
    private int numWordsInRawInput;
    private int numInvalidWords;

    public ResultFetchedEvent(String result, String error, int numWordsInRawInput, int numInvalidWords) {
        this.result = result;
        this.error = error;
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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
